package com.yanzu.module.member.service.order;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.common.util.collection.CollectionUtils;
import com.yanzu.framework.common.util.date.DateUtils;
import com.yanzu.module.member.controller.app.order.vo.*;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.couponinfo.CouponInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.payorder.PayOrderService;
import com.yanzu.module.system.api.social.SocialUserApi;
import com.yanzu.module.system.enums.social.SocialTypeEnum;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

@Service
@Validated
@Slf4j
public class AppOrderServiceImpl implements AppOrderService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private StoreUserMapper storeUserMapper;

    @Resource
    private CouponInfoMapper couponInfoMapper;

    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private DeviceService deviceService;

    @Resource
    private UserMoneyBillMapper userMoneyBillMapper;

    @Resource
    private ClearInfoMapper clearInfoMapper;

    @Resource
    private SocialUserApi socialUserApi;

    @Resource
    private PayOrderService payOrderService;

    @Autowired
    private WxPayService wxService;

    @Value("${wx.pay.returnUrl}")
    private String returnUrl;

    /**
     * @param roomId        房间id
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param couponId      优惠券id
     * @param ignoreOrderId 忽略校验的订单id，用于更换房间 或者提前开始订单
     * @param wxpay         是否微信下单
     * @return
     */
    @Override
    public WxPayOrderRespVO preOrder(Long roomId, Date startTime, Date endTime, Long couponId, Long ignoreOrderId, boolean wxpay) {
        Date now = new Date();
        //参数校验
        if (startTime.before(now)) {
            //开始时间在当前之前，不能超过5分钟  不然间隔太久了
            long l = (now.getTime() - startTime.getTime()) / 1000 / 60;
            if (l > 6) {
                throw exception(ORDER_START_TIME_GT_NOW_ERROR);
            }
        }
        if (startTime.after(endTime)) {
            throw exception(ORDER_START_TIME_GT_END_ERROR);
        }
        //检查是不是0.5小时为单位
        long l = (endTime.getTime() - startTime.getTime()) / 1000 / 60;
        if (l % 30 != 0) {
            throw exception(TIME_UNIT_ERROR);
        }
        //订单不能超过24小时  不然间隔太久了
        if (l / 30 > 48) {
            throw exception(ORDER_MAX_END_TIME_ERROR);
        }
        //检查时间有没有超过提前5天的范围
        Instant instant1 = startTime.toInstant();
        Instant instant2 = now.toInstant();
        ZonedDateTime zonedDateTime1 = instant1.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTime2 = instant2.atZone(ZoneId.systemDefault());
        Duration duration = Duration.between(zonedDateTime1, zonedDateTime2);
        long days = duration.toDays();
        if (days > 5) {
            throw exception(ORDER_START_TIME_MAX_ERROR);
        }
        //查询出该房间 所有的订单 以及不可用的时间段
        List<OrderInfoDO> orderInfoList = orderInfoMapper.getByRoomId(roomId, ignoreOrderId);
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        //构建出不可用的时间区间
        List<TimeSlotVO> timeSlotVOList = new ArrayList<>();
        if (!CollectionUtils.isAnyEmpty(orderInfoList)) {
            for (OrderInfoDO orderInfoDO : orderInfoList) {
                TimeSlotVO timeSlotVO = new TimeSlotVO();
                timeSlotVO.setStartTime(orderInfoDO.getStartTime());
                timeSlotVO.setEndTime(orderInfoDO.getEndTime());
                timeSlotVOList.add(timeSlotVO);
            }
        }
        if (!ObjectUtils.isEmpty(roomInfoDO.getBanTimeStart()) && !ObjectUtils.isEmpty(roomInfoDO.getBanTimeEnd())) {
            //从今天起，加5天的禁用时间进去
            String todayStr = DateUtils.dateToStr(now, DateUtils.FORMAT_YEAR_MONTH_DAY);
            //拼接开始时间和结束时间
            String startTimeStr = todayStr + " " + roomInfoDO.getBanTimeStart();
            String endTimeStr = todayStr + " " + roomInfoDO.getBanTimeEnd();
            //转回date
            Date banStart = DateUtils.strToDate(startTimeStr, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE);
            Date banEnd = DateUtils.strToDate(endTimeStr, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE);
            for (int i = 0; i < 5; i++) {
                TimeSlotVO timeSlotVO = new TimeSlotVO();
                timeSlotVO.setStartTime(banStart);
                timeSlotVO.setEndTime(banEnd);
                timeSlotVOList.add(timeSlotVO);
                banStart = DateUtils.addDate(banStart, Calendar.DAY_OF_YEAR, 1);
                banEnd = DateUtils.addDate(banStart, Calendar.DAY_OF_YEAR, 1);
            }
        }
        //查看是否需要校验 如果是空的 代表可以下单 就不校验了
        if (!CollectionUtils.isAnyEmpty(timeSlotVOList)) {
            //需要校验
            for (TimeSlotVO timeSlotVO : timeSlotVOList) {
                //如果下单时间大于不可用时间的开始时间， 并且不可用时间的开始时间小于订单的结束时间，那么就不能下单
                if (startTime.before(timeSlotVO.getEndTime()) && timeSlotVO.getStartTime().before(endTime)) {
                    //存在交集
                    throw exception(ORDER_TIME_CHECK_ERROR);
                }
            }
        }
        BigDecimal mathPrice = mathPrice(roomInfoDO.getPrice(), startTime, endTime, couponId);
        int price = mathPrice.multiply(BigDecimal.valueOf(100D)).intValue();//价格转成分为单位
        String orderNo = getOrderNo();
        WxPayOrderRespVO respVO = new WxPayOrderRespVO();
        respVO.setPrice(price);
        if (wxpay) {
            //需要微信下单  先获取到该用户的openId
            String openId = socialUserApi.getUserOpenIdByType(getLoginUserId(), SocialTypeEnum.WECHAT_MINI_APP.getType());
            if (ObjectUtils.isEmpty(openId)) {
                throw exception(AUTH_USER_BIND_MINIAPP_ERROR);
            }
            //生成微信支付的订单
            WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
            wxPayUnifiedOrderRequest.setBody("微信支付订单");
            wxPayUnifiedOrderRequest.setOutTradeNo(orderNo);
            wxPayUnifiedOrderRequest.setTotalFee(price);
            wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
            wxPayUnifiedOrderRequest.setNotifyUrl(returnUrl);
            wxPayUnifiedOrderRequest.setTradeType("JSAPI");
            wxPayUnifiedOrderRequest.setOpenid(openId);
            wxPayUnifiedOrderRequest.setSignType("HMAC-SHA256");
//            wxPayUnifiedOrderRequest.setTimeExpire()
            try {
//                WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxService.unifiedOrder(wxPayUnifiedOrderRequest);
                WxPayMpOrderResult wxPayMpOrderResult = wxService.createOrder(wxPayUnifiedOrderRequest);
                respVO.setPkg(wxPayMpOrderResult.getPackageValue());
                respVO.setAppId(wxPayMpOrderResult.getAppId());
                respVO.setNonceStr(wxPayMpOrderResult.getNonceStr());
                respVO.setPaySign(wxPayMpOrderResult.getPaySign());
                respVO.setSignType(wxPayMpOrderResult.getSignType());
                respVO.setTimeStamp(wxPayMpOrderResult.getTimeStamp());
            } catch (WxPayException e) {
                e.printStackTrace();
//                throw new RuntimeException(e);
                throw exception(USER_WEIXIN_PAY_ERROR);
            }
            payOrderService.create(getLoginUserId(), orderNo, "微信支付订单", price);
        }
        return respVO;
    }

    @Override
    public BigDecimal mathPrice(BigDecimal price, Date startTime, Date endTime, Long couponId) {
        long l = (endTime.getTime() - startTime.getTime()) / 1000 / 60;
        BigDecimal hour = BigDecimal.valueOf(l / 60.0);
        //计算价格 单价*时长
        BigDecimal totalPrice = price.multiply(hour);
        //判断使用优惠券的情况
        if (!ObjectUtils.isEmpty(couponId)) {
            //有使用
            CouponInfoDO couponInfoDO = couponInfoMapper.getByUserIdAndCouponId(getLoginUserId(), couponId);
            if (ObjectUtils.isEmpty(couponInfoDO)) {
                throw exception(COUPON_NOT_FOUND_ERROR);
            }
            //判断类型
            switch (couponInfoDO.getType()) {
                case 1://1抵扣券
                    //判断门槛
                    if (couponInfoDO.getMinUsePrice().compareTo(hour) < 0) {
                        throw exception(COUPON_MIN_USER_PRICE_ERROR);
                    }
                    //抵扣 并重新算价格
                    if (couponInfoDO.getPrice().compareTo(hour) >= 0) {
                        //直接抵扣完，价格设置为0
                        totalPrice = BigDecimal.ZERO;
                    } else {
                        hour.subtract(couponInfoDO.getPrice());
                        totalPrice = price.multiply(hour);
                    }
                    break;
                case 2://2满减券
                    //判断门槛
                    if (couponInfoDO.getMinUsePrice().compareTo(totalPrice) < 0) {
                        throw exception(COUPON_MIN_USER_PRICE_ERROR);
                    }
                    //抵扣 并重新算价格
                    if (couponInfoDO.getPrice().compareTo(totalPrice) >= 0) {
                        //直接抵扣完，价格设置为0
                        totalPrice = BigDecimal.ZERO;
                    } else {
                        totalPrice = totalPrice.subtract(couponInfoDO.getPrice());
                    }
                    break;
            }
        }
        return totalPrice;
    }

    private void addPayRecord(BigDecimal price, Integer type, Integer moneyType, BigDecimal totalMoney, BigDecimal totalGiftMoney, String remark, Long userId) {
        UserMoneyBillDO userMoneyBillDO = new UserMoneyBillDO();
        userMoneyBillDO.setMoney(price);
        userMoneyBillDO.setType(type);
        userMoneyBillDO.setMoneyType(moneyType);
        userMoneyBillDO.setTotalMoney(totalMoney);
        userMoneyBillDO.setTotalGiftMoney(totalGiftMoney);
        userMoneyBillDO.setRemark(remark);
        userMoneyBillDO.setUserId(userId);
        userMoneyBillMapper.insert(userMoneyBillDO);
    }

    @Override
    @Transactional
    public void save(OrderSaveReqVO reqVO) {
        Long userId = getLoginUserId();
        //二次检查 下单时时间是必须大于4小时的
        long l = (reqVO.getEndTime().getTime() - reqVO.getStartTime().getTime()) / 1000 / 60;
        if (l < 240) {
            throw exception(ORDER_TIME_MIN_ERROR);
        }
        //定义一些参数 备用
        String orderNo = getOrderNo();
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
        BigDecimal oldPrice = BigDecimal.valueOf(l / 60.0).multiply(roomInfoDO.getPrice());//原价
        //下单之前仍然再检查一遍 并计算出应付总金额
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(reqVO.getRoomId(), reqVO.getStartTime(), reqVO.getEndTime(), reqVO.getCouponId(), null, false);
        BigDecimal totalPrice = BigDecimal.valueOf(wxPayOrderRespVO.getPrice() * 100);
        //判断是否有填团购券
        if (!ObjectUtils.isEmpty(reqVO.getGroupPayNo())) {
            //校验团购券 todo...

        } else {
            //非团购支付 判断支付方式
            switch (reqVO.getPayType()) {
                case 1://微信
                    if (ObjectUtils.isEmpty(reqVO.getWeixinOrderNo())) {
                        throw exception(ORDER_WEIXIN_PAY_ERROR);
                    }
                    //有支付单号，再验证支付是否成功
                    PayOrderDO payOrderByPayNo = payOrderService.getPayOrderByPayNo(reqVO.getWeixinOrderNo());
                    if (ObjectUtils.isEmpty(payOrderByPayNo) || !queryWxOrder(payOrderByPayNo.getOrderNo()) || !payOrderByPayNo.getPayStatus()) {
                        throw exception(ORDER_WEIXIN_PAY_ERROR);
                    }
                    break;
                case 2://余额
                    //先扣钱包余额
                    MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
                    if (memberUserDO.getBalance().compareTo(totalPrice) >= 0) {
                        //钱够 直接扣
                        synchronized (this) {
                            memberUserDO.setBalance(memberUserDO.getBalance().subtract(totalPrice));
                            memberUserMapper.updateById(memberUserDO);
                        }
                        //增加付款记录
                        addPayRecord(totalPrice, AppEnum.user_money_bill_type.PAY.getValue(), 1, memberUserDO.getBalance(), null, "订单：" + orderNo + ",支付", userId);
                    } else {
                        //钱不够  看看有没有赠送余额 加起来判断够不够
                        StoreUserDO byUserIdAndStoreId = storeUserMapper.getByUserIdAndStoreId(userId, roomInfoDO.getStoreId());
                        if (ObjectUtils.isEmpty(byUserIdAndStoreId)) {
                            //没有 报错余额不足
                            throw exception(MEMBER_BALANCE_MIN_ERROR);
                        } else {
                            BigDecimal userBalance = memberUserDO.getBalance();
                            BigDecimal added = memberUserDO.getBalance().add(byUserIdAndStoreId.getGiftBalance());
                            //有 加起余额一起判断
                            if (added.compareTo(totalPrice) >= 0) {
                                //钱够 先扣赠送余额 再扣余额
                                BigDecimal subtract = totalPrice.subtract(memberUserDO.getBalance());//要从赠送余额扣的钱
                                memberUserDO.setBalance(BigDecimal.ZERO);
                                byUserIdAndStoreId.setGiftBalance(byUserIdAndStoreId.getGiftBalance().subtract(subtract));
                                synchronized (this) {
                                    memberUserMapper.updateById(memberUserDO);
                                    storeUserMapper.updateById(byUserIdAndStoreId);
                                }
                                //增加付款记录
                                addPayRecord(userBalance, AppEnum.user_money_bill_type.PAY.getValue(), 1, BigDecimal.ZERO, null, "订单：" + orderNo + ",支付", userId);
                                addPayRecord(subtract, AppEnum.user_money_bill_type.PAY.getValue(), 2, byUserIdAndStoreId.getGiftBalance(), null, "订单：" + orderNo + ",支付", userId);
                            } else {
                                throw exception(MEMBER_BALANCE_MIN_ERROR);
                            }
                        }
                    }
                    break;
                default:
                    throw exception(PAY_TYPE_ERROR);
            }
        }
        //生成订单，并修改房间状态
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        orderInfoDO.setOrderNo(orderNo);
        orderInfoDO.setStoreId(roomInfoDO.getStoreId());
        orderInfoDO.setRoomId(roomInfoDO.getRoomId());
        orderInfoDO.setUserId(userId);
        orderInfoDO.setStartTime(reqVO.getStartTime());
        orderInfoDO.setEndTime(reqVO.getEndTime());
        orderInfoDO.setPrice(oldPrice);
        orderInfoDO.setPayPrice(totalPrice);
        orderInfoDO.setRefundPrice(BigDecimal.ZERO);
        orderInfoDO.setPayType(reqVO.getPayType());
        orderInfoDO.setGroupPayNo(reqVO.getGroupPayNo());
        orderInfoDO.setCouponId(reqVO.getCouponId());
        orderInfoMapper.insert(orderInfoDO);
        //如果房间状态不是进行中，就改成已预定
        if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.USED.getValue()) != 0) {
            roomInfoDO.setStatus(AppEnum.room_status.PENDDING.getValue());
            roomInfoMapper.updateById(roomInfoDO);
        }


    }

    private String getOrderNo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDate = currentDateTime.format(dateFormatter);
        Random random = new Random();
        int randomNum = random.nextInt(100000000);
        String randomNumString = String.format("%08d", randomNum);
        return currentDate + randomNumString;
    }

    @Override
    @Transactional
    public void renew(OrderRenewalReqVO reqVO) {
        Long userId = getLoginUserId();
        if (reqVO.getMinutes() < 1 || reqVO.getMinutes() % 30 != 0) {
            throw exception(TIME_UNIT_ERROR);
        }
        //把订单查出来
        OrderInfoDO orderInfoDO = orderInfoMapper.selectById(reqVO.getOrderId());
        //未开始=0 进行中=1  已完成=2  已取消=3
        switch (orderInfoDO.getStatus()) {
            case 0:
            case 1:
                //未开始和进行中  直接续费
                break;
            case 2://已完成，5分钟内可以续费，超过5分钟只能重新下单
                if (((new Date().getTime() - orderInfoDO.getEndTime().getTime()) / 1000 / 60) > 5) {
                    throw exception(ORDER_STATUS_FINISH_OPRATION_ERROR);
                }
                break;
            case 3://已经取消，不能续费
                throw exception(ORDER_STATUS_CANCEL_OPRATION_ERROR);
        }
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(orderInfoDO.getRoomId());
        BigDecimal oldPrice = BigDecimal.valueOf(reqVO.getMinutes() / 60.0).multiply(roomInfoDO.getPrice());//原价
        //下单之前仍然再检查一遍 并计算出应付总金额
        Date startTime = orderInfoDO.getEndTime();
        Date endTime = DateUtils.addDate(orderInfoDO.getEndTime(), Calendar.MINUTE, reqVO.getMinutes());
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(orderInfoDO.getRoomId(), startTime, endTime, null, null, false);
        BigDecimal totalPrice = BigDecimal.valueOf(wxPayOrderRespVO.getPrice() * 100);
        switch (reqVO.getPayType()) {
            case 1://微信
                if (ObjectUtils.isEmpty(reqVO.getWeixinOrderNo())) {
                    throw exception(ORDER_WEIXIN_PAY_ERROR);
                }
                //有支付单号，再验证支付是否成功
                PayOrderDO payOrderByPayNo = payOrderService.getPayOrderByPayNo(reqVO.getWeixinOrderNo());
                if (ObjectUtils.isEmpty(payOrderByPayNo) || !queryWxOrder(payOrderByPayNo.getOrderNo()) || !payOrderByPayNo.getPayStatus()) {
                    throw exception(ORDER_WEIXIN_PAY_ERROR);
                }
                break;
            case 2://余额
                //先扣钱包余额
                MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
                if (memberUserDO.getBalance().compareTo(totalPrice) >= 0) {
                    //钱够 直接扣
                    synchronized (this) {
                        memberUserDO.setBalance(memberUserDO.getBalance().subtract(totalPrice));
                        memberUserMapper.updateById(memberUserDO);
                    }
                    //增加付款记录
                    addPayRecord(totalPrice, AppEnum.user_money_bill_type.PAY.getValue(), 1, memberUserDO.getBalance(), null, "订单：" + orderInfoDO.getOrderNo() + ",续费", userId);

                } else {
                    //钱不够  看看有没有赠送余额 加起来判断够不够
                    StoreUserDO byUserIdAndStoreId = storeUserMapper.getByUserIdAndStoreId(userId, roomInfoDO.getStoreId());
                    if (ObjectUtils.isEmpty(byUserIdAndStoreId)) {
                        //没有 报错余额不足
                        throw exception(MEMBER_BALANCE_MIN_ERROR);
                    } else {
                        BigDecimal userBalance = memberUserDO.getBalance();
                        //有 加起余额一起判断
                        BigDecimal added = memberUserDO.getBalance().add(byUserIdAndStoreId.getGiftBalance());
                        if (added.compareTo(totalPrice) >= 0) {
                            //钱够 先扣赠送余额 再扣余额
                            BigDecimal subtract = totalPrice.subtract(memberUserDO.getBalance());//要从赠送余额扣的钱
                            memberUserDO.setBalance(BigDecimal.ZERO);
                            byUserIdAndStoreId.setGiftBalance(byUserIdAndStoreId.getGiftBalance().subtract(subtract));
                            synchronized (this) {
                                memberUserMapper.updateById(memberUserDO);
                                storeUserMapper.updateById(byUserIdAndStoreId);
                            }
                            //增加付款记录
                            addPayRecord(userBalance, AppEnum.user_money_bill_type.PAY.getValue(), 1, BigDecimal.ZERO, null, "订单：" + orderInfoDO.getOrderNo() + ",续费", userId);
                            addPayRecord(subtract, AppEnum.user_money_bill_type.PAY.getValue(), 2, byUserIdAndStoreId.getGiftBalance(), null, "订单：" + orderInfoDO.getOrderNo() + ",续费", userId);
                        } else {
                            throw exception(MEMBER_BALANCE_MIN_ERROR);
                        }
                    }
                }
                break;
            default:
                throw exception(PAY_TYPE_ERROR);
        }
        //支付完了，增加订单的结束时间
        orderInfoDO.setEndTime(DateUtils.addDate(orderInfoDO.getEndTime(), Calendar.MINUTE, reqVO.getMinutes()));
        //如果状态是已完成，则状态改成进行中 并触发一次开房间门操作，以实现通电
        if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.FINISH.getValue()) == 0) {
            orderInfoDO.setStatus(AppEnum.order_status.START.getValue());
        }
        //增加订单金额
        orderInfoDO.setPrice(orderInfoDO.getPrice().add(oldPrice));
        //增加已支付的金额
        orderInfoDO.setPayPrice(orderInfoDO.getPayPrice().add(totalPrice));
        orderInfoMapper.updateById(orderInfoDO);
        if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.FINISH.getValue()) == 0) {
            orderInfoDO.setStatus(AppEnum.order_status.START.getValue());
            deviceService.openRoomDoor(roomInfoDO.getRoomId(), orderInfoDO.getOrderId(), 1);
        }
        //todo...如果有已接单的保洁订单 发消息通知保洁时间延后了

    }


    @Override
    public PageResult<OrderListRespVO> getOrderPage(OrderPageReqVO reqVO) {
        PageHelper.startPage(reqVO);
        List<OrderListRespVO> list = orderInfoMapper.getOrderPage(reqVO);
        PageInfo<OrderListRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    public OrderInfoAppRespVO getOrderInfo(Long orderId) {
        return orderInfoMapper.getOrderInfo(orderId);
    }

    @Override
    public String getRoomImgs(Long roomId) {
        return roomInfoMapper.getRoomImgs(roomId);
    }

    @Override
    @Transactional
    public void changeRoom(Long orderId, Long roomId) {
        //取出当前订单信息
        OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
        Long loginUserId = getLoginUserId();
        //只能操作自己的订单
        if (orderInfoDO.getUserId().compareTo(loginUserId) != 0) {
            throw exception(OPRATION_ERROR);
        }
        //只有未开始的订单才能更换房间
        if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
            //只能更换到小于等于当前房间级别的
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
            RoomInfoDO roomInfoDO1 = roomInfoMapper.selectById(orderInfoDO.getRoomId());
            if (roomInfoDO.getType() > roomInfoDO1.getType()) {
                throw exception(ORDER_CHANGE_ROOM_ERROR);
            } else {
                //检查是否可用
                preOrder(roomId, orderInfoDO.getStartTime(), orderInfoDO.getEndTime(), null, null, false);
                //开始更换
                orderInfoDO.setRoomId(roomId);
                orderInfoMapper.updateById(orderInfoDO);
                //改新房间的状态  如果房间是空闲，则改成已预订
                if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.ENABLE.getValue()) == 0) {
                    roomInfoDO.setStatus(AppEnum.room_status.PENDDING.getValue());
                }
                //改旧房间的状态  如果房间是已预订，并且没有其他订单，则改回空闲
                if (roomInfoDO1.getStatus().compareTo(AppEnum.room_status.PENDDING.getValue()) == 0) {
                    List<OrderInfoDO> orderInfoDOList = orderInfoMapper.getByRoomId(roomInfoDO1.getRoomId(), null);
                    if (!CollectionUtils.isAnyEmpty(orderInfoDOList)) {
                        roomInfoDO1.setStatus(AppEnum.room_status.ENABLE.getValue());
                        roomInfoMapper.updateById(roomInfoDO1);
                    }
                }
            }
        } else {
            throw exception(CLEAR_ORDER_STATUS_ERROR);
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
        Long loginUserId = getLoginUserId();
        //只能操作自己的订单
        if (orderInfoDO.getUserId().compareTo(loginUserId) != 0) {
            throw exception(OPRATION_ERROR);
        }
        Date now = new Date();
        //只有未开始的订单才能取消
        if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
            //订单开始时间不足30分钟  将无法取消
            if ((orderInfoDO.getStartTime().getTime() - now.getTime()) < 1000 * 60 * 30) {
                throw exception(ORDER_CANCEL_TIMEOUT_ERROR);
            }
            //取消
            orderInfoDO.setStatus(AppEnum.order_status.CANCEL.getValue());
            orderInfoMapper.updateById(orderInfoDO);
            //判断支付方式
            if (!ObjectUtils.isEmpty(orderInfoDO.getGroupPayNo())) {
                //团购支付的，操作团购退款 todo...
            } else {
                if (orderInfoDO.getPayType().compareTo(AppEnum.order_pay_type.WEIXIN.getValue()) == 0) {
                    //微信退款 todo...

                } else {
                    //余额退款  把支付记录找出来
                    List<UserMoneyBillDO> userMoneyBillDOList = userMoneyBillMapper.getPayByOrderNo(orderInfoDO.getOrderNo(), loginUserId);
                    if (!org.springframework.util.CollectionUtils.isEmpty(userMoneyBillDOList)) {
                        for (UserMoneyBillDO billDO : userMoneyBillDOList) {
                            UserMoneyBillDO newUserMoneyBillDO = new UserMoneyBillDO();
                            BeanUtils.copyProperties(billDO, newUserMoneyBillDO);
                            newUserMoneyBillDO.setId(null);
                            newUserMoneyBillDO.setCreateTime(null);
                            newUserMoneyBillDO.setCreator(null);
                            newUserMoneyBillDO.setUpdateTime(null);
                            newUserMoneyBillDO.setUpdater(null);
                            newUserMoneyBillDO.setType(4);//改成退款状态
                            if (billDO.getMoneyType().intValue() == 1) {
                                //账户余额  加回去
                                MemberUserDO memberUserDO = memberUserMapper.selectById(loginUserId);
                                memberUserDO.setBalance(memberUserDO.getBalance().add(billDO.getMoney()));
                                synchronized (this) {
                                    memberUserMapper.updateById(memberUserDO);
                                }
                                newUserMoneyBillDO.setTotalMoney(memberUserDO.getBalance());
                            } else if (billDO.getMoneyType().intValue() == 2) {
                                //赠送余额  加回去
                                StoreUserDO byUserIdAndStoreId = storeUserMapper.getByUserIdAndStoreId(loginUserId, orderInfoDO.getStoreId());
                                byUserIdAndStoreId.setGiftBalance(byUserIdAndStoreId.getGiftBalance().add(billDO.getMoney()));
                                synchronized (this) {
                                    storeUserMapper.updateById(byUserIdAndStoreId);
                                }
                                newUserMoneyBillDO.setTotalGiftMoney(byUserIdAndStoreId.getGiftBalance());
                            } else {
                                throw exception(OPRATION_ERROR);
                            }
                            userMoneyBillMapper.insert(newUserMoneyBillDO);
                        }
                    }
                }
            }
            //取消后  如果后面没有预约了，把房间状态改回空闲
            List<OrderInfoDO> orderInfoDOList = orderInfoMapper.getByRoomId(orderInfoDO.getRoomId(), null);
            if (org.springframework.util.CollectionUtils.isEmpty(orderInfoDOList)) {
                roomInfoMapper.updateStatusById(AppEnum.room_status.ENABLE.getValue(), orderInfoDO.getRoomId());
            }
        } else {
            throw exception(ORDER_CANCEL_OPRATION_ERROR);
        }

    }

    @Override
    @Transactional
    public void startOrder(Long orderId) {
        OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
        if (ObjectUtils.isEmpty(orderInfoDO)) {
            throw exception(DATA_NOT_EXISTS);
        }
        Long loginUserId = getLoginUserId();
        //只能操作自己的订单
        if (orderInfoDO.getUserId().compareTo(loginUserId) != 0) {
            throw exception(OPRATION_ERROR);
        }
        Date now = new Date();
        //只有未开始的订单才能开始
        if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
            //判断当前的时间是否在订单开始时间之前
            if (now.before(orderInfoDO.getStartTime())) {
                //早于开始时间 判断一下是否能提前开始
                //新的结束时间 等于当前时间加上订单的时长
                long l = now.getTime() + (orderInfoDO.getEndTime().getTime() - orderInfoDO.getStartTime().getTime());
                Date endTime = new Date(l);
                //校验时间冲突
                preOrder(orderInfoDO.getRoomId(), now, endTime, null, null, false);
                //校验通过 更改订单的开始和完成时间
                orderInfoDO.setStartTime(now);
                orderInfoDO.setEndTime(endTime);
            }
            //开始订单
            orderInfoDO.setStatus(AppEnum.order_status.START.getValue());
            orderInfoMapper.updateById(orderInfoDO);
            //房间改为进行中
            roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), orderInfoDO.getRoomId());
            //新增保洁订单
            ClearInfoDO clearInfoDO = new ClearInfoDO();
            clearInfoDO.setOrderId(orderId);
            clearInfoDO.setStoreId(orderInfoDO.getStoreId());
            clearInfoDO.setOrderNo(orderInfoDO.getOrderNo());
            clearInfoDO.setRoomId(orderInfoDO.getRoomId());
            clearInfoMapper.insert(clearInfoDO);
            //开门开电
            deviceService.openRoomDoor(orderInfoDO.getRoomId(), null, 4);
            //播放欢迎语
            deviceService.runSound(orderInfoDO.getRoomId(), 4);
        } else {
            throw exception(ORDER_START_OPRATION_ERROR);
        }

    }

    /**
     * 订单处理的定时任务，每分钟执行一次， 用于到时间开始订单 或者 结束订单
     */
    @Override
    @Transactional
    @Synchronized
    public void executeOrderJob() {
        log.info("=====     开始执行订单定时检查任务     =====");
        Date now = new Date();
        //取出所有未开始的订单
        List<OrderInfoDO> list1 = orderInfoMapper.getByStatus(AppEnum.order_status.PENDING.getValue());
        //如果存在开始时间已经大于现在的时间的 则把订单状态改为开始
        if (!org.springframework.util.CollectionUtils.isEmpty(list1)) {
            List<String> roomIds = new ArrayList<>();
            //新增保洁订单
            List<ClearInfoDO> clearInfoDOList = new ArrayList<>();
            list1.forEach(x -> {
                if (x.getStartTime().after(now)) {
                    //开始订单
                    x.setStatus(AppEnum.order_status.START.getValue());
                    //房间改为进行中
                    roomIds.add(String.valueOf(x.getRoomId()));
                    //新增保洁订单
                    ClearInfoDO clearInfoDO = new ClearInfoDO();
                    clearInfoDO.setOrderId(x.getOrderId());
                    clearInfoDO.setStoreId(x.getStoreId());
                    clearInfoDO.setOrderNo(x.getOrderNo());
                    clearInfoDO.setRoomId(x.getRoomId());
                    clearInfoDOList.add(clearInfoDO);
                }
            });
            orderInfoMapper.updateBatch(list1);
            if (!org.springframework.util.CollectionUtils.isEmpty(roomIds)) {
                roomInfoMapper.updateStatusByIds(AppEnum.room_status.USED.getValue(), roomIds.stream().collect(Collectors.joining(",")));
                clearInfoMapper.insertBatch(clearInfoDOList);
            }
        }
        //取出所有进行中的订单
        List<OrderInfoDO> listStart = orderInfoMapper.getByStatus(AppEnum.order_status.START.getValue());
        //如果存在结束时间已经小于现在的时间的 则把订单状态改为完成
        if (!org.springframework.util.CollectionUtils.isEmpty(listStart)) {
            List<String> roomIds = new ArrayList<>();
            listStart.forEach(x -> {
                if (x.getEndTime().before(now)) {
                    x.setStatus(AppEnum.order_status.FINISH.getValue());
                    //房间改为待保洁
                    roomIds.add(String.valueOf(x.getRoomId()));
                    //关门关电
                    deviceService.closeRoomDoor(x.getRoomId(), null, 4);
                } else {
                    //如果订单结束时间  还剩30分钟，发送第一次提醒
                    long l = (now.getTime() - x.getEndTime().getTime()) / 1000 / 60;
                    if (l == 30) {
                        deviceService.runSound(x.getRoomId(), 2);
                    } else if (l == 15) {
                        deviceService.runSound(x.getRoomId(), 3);
                    } else if (l == 5) {
                        deviceService.runSound(x.getRoomId(), 4);
                    }
                }
            });
            if (!org.springframework.util.CollectionUtils.isEmpty(roomIds)) {
                roomInfoMapper.updateStatusByIds(AppEnum.room_status.CLEAR.getValue(), roomIds.stream().collect(Collectors.joining(",")));
            }
        }
    }

    @Override
    public boolean queryWxOrder(String orderNo) {
        try {
            WxPayOrderQueryResult wxPayOrderQueryResult = wxService.queryOrder(null, orderNo);
            String tradeState = wxPayOrderQueryResult.getTradeState();
            String returnCode = wxPayOrderQueryResult.getReturnCode();
            String resultCode = wxPayOrderQueryResult.getResultCode();
            return tradeState.equals("SUCCESS") && returnCode.equals("SUCCESS") && resultCode.equals("SUCCESS");
        } catch (WxPayException e) {
//            throw new RuntimeException(e);
            return false;
        }
    }

//    @Override
//    @Transactional
//    public void closeOrder(Long orderId) {
//        OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
//        Long loginUserId = getLoginUserId();
//        //只能操作自己的订单
//        if (orderInfoDO.getUserId().compareTo(loginUserId) != 0) {
//            throw exception(OPRATION_ERROR);
//        }
//        //状态改为完成
//        orderInfoDO.setStatus(AppEnum.order_status.FINISH.getValue());
//
//    }
}
