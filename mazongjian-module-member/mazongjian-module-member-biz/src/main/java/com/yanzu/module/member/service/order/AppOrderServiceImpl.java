package com.yanzu.module.member.service.order;

import cn.hutool.json.JSONObject;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
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
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.couponinfo.CouponInfoMapper;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.payorder.PayOrderMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.AppUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.meituan.MeituanService;
import com.yanzu.module.member.service.payorder.PayOrderService;
import com.yanzu.module.member.service.workwx.WorkWxService;
import com.yanzu.module.system.api.social.SocialUserApi;
import com.yanzu.module.system.enums.social.SocialTypeEnum;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.AppEnum.PAY_ORDER_REDIS_SET;
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
    private WxPayService wxPayService;

    @Resource
    private MeituanService meituanService;
    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private DiscountRulesMapper discountRulesMapper;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private AppUserMapper appUserMapper;
    @Resource
    private WorkWxService workWxService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${wx.pay.returnUrl}")
    private String returnUrl;

    /**
     * @param roomId        房间id
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param couponInfoDO  优惠券
     * @param ignoreOrderId 忽略校验的订单id，用于更换房间 或者提前开始订单
     * @param wxpay         是否微信下单
     * @return
     */
    @Override
    public WxPayOrderRespVO preOrder(Long roomId, Date startTime, Date endTime, CouponInfoDO couponInfoDO, Long ignoreOrderId, boolean wxpay) {
        Date now = new Date();
        //参数校验
        if (checkTongxiao(startTime, endTime)) {
            // 通宵场  不校验开始时间是否早于当前时间 但是如果有用通宵优惠券  要判断优惠券可不可用
            if (!ObjectUtils.isEmpty(couponInfoDO)) {
                if (couponInfoDO.getCouponName().indexOf("通宵") == -1) {
                    throw exception(TONGXIAO_COUPON_USE_ERROR);
                }
            }
        } else {
            if (startTime.before(now)) {
                //开始时间在当前之前，不能超过5分钟  不然间隔太久了
                long l = (now.getTime() - startTime.getTime()) / 1000 / 60;
                if (l > 6) {
                    throw exception(ORDER_START_TIME_LT_NOW_ERROR);
                }
            }
            //普通场 不能使用通宵券
            if (!ObjectUtils.isEmpty(couponInfoDO)) {
                if (couponInfoDO.getCouponName().indexOf("通宵") != -1) {
                    throw exception(TONGXIAO_COUPON_USE_ERROR);
                }
            }
        }
        if (startTime.after(endTime)) {
            throw exception(ORDER_START_TIME_GT_END_ERROR);
        }
//
//        //订单不能超过24小时  不然间隔太久了
//        if (l / 30 > 48) {
//            throw exception(ORDER_MAX_END_TIME_ERROR);
//        }
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
        List<TimeRange> disabledTimeRanges = new ArrayList<>();
        //先把订单中的时间进行处理
        if (!CollectionUtils.isAnyEmpty(orderInfoList)) {
            for (OrderInfoDO orderInfoDO : orderInfoList) {
                disabledTimeRanges.add(new TimeRange(DateUtils.of(orderInfoDO.getStartTime()), DateUtils.of(orderInfoDO.getEndTime())));
            }
        }
        //再处理每天有禁用时间的情况
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        if (!ObjectUtils.isEmpty(roomInfoDO.getBanTimeStart()) && !ObjectUtils.isEmpty(roomInfoDO.getBanTimeStart())) {
            // 禁用时间段列表，包含禁用开始时间和结束时间 new TimeRange("02:00", "08:00")
            LocalTime bstart = LocalTime.parse(roomInfoDO.getBanTimeStart());
            LocalTime bend = LocalTime.parse(roomInfoDO.getBanTimeEnd());
            // 遍历日期范围内的每一天'
            for (int i = 0; i < 5; i++) {
                // 判断是否跨越两天
                if (bend.isBefore(bstart)) {
                    // 添加禁用时间范围：从开始时间到当天最后一秒
                    disabledTimeRanges.add(new TimeRange(currentDate.atTime(bstart), currentDate.atTime(LocalTime.MAX)));
                    // 添加禁用时间范围：从零点到结束时间
                    disabledTimeRanges.add(new TimeRange(currentDate.atTime(LocalTime.MIDNIGHT), currentDate.atTime(bend)));
                } else {
                    // 添加禁用时间范围：从开始时间到结束时间
                    disabledTimeRanges.add(new TimeRange(currentDate.atTime(bstart), currentDate.atTime(bend)));
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        //查看是否需要校验 如果是空的 代表可以下单 就不校验了
        if (!CollectionUtils.isAnyEmpty(disabledTimeRanges)) {
            //需要校验
            for (TimeRange timeRange : disabledTimeRanges) {
                //如果下单时间大于不可用时间的开始时间， 并且不可用时间的开始时间小于订单的结束时间，那么就不能下单
                if (startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(timeRange.getEnd())
                        && timeRange.getStart().isBefore(endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
                    //存在交集
                    throw exception(ORDER_TIME_CHECK_ERROR);
                }
            }
        }
        BigDecimal mathPrice = mathPrice(roomInfoDO.getPrice(), startTime, endTime, couponInfoDO);
        int price = mathPrice.multiply(BigDecimal.valueOf(100D)).intValue();//价格转成分为单位
        String orderNo = getOrderNo();
        WxPayOrderRespVO respVO = new WxPayOrderRespVO();
        respVO.setPrice(price);
        respVO.setOrderNo(orderNo);
        if (wxpay) {
            if (price > 0) {
                //需要微信下单  先获取到该用户的openId
                String openId = socialUserApi.getUserOpenIdByType(getLoginUserId(), SocialTypeEnum.WECHAT_MINI_APP.getType());
                if (ObjectUtils.isEmpty(openId)) {
                    throw exception(AUTH_USER_BIND_MINIAPP_ERROR);
                }
                //生成微信支付的订单
                WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
                wxPayUnifiedOrderRequest.setBody("预订支付订单");
                wxPayUnifiedOrderRequest.setOutTradeNo(orderNo);
                wxPayUnifiedOrderRequest.setTotalFee(price);
                wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
                wxPayUnifiedOrderRequest.setNotifyUrl(returnUrl);
                wxPayUnifiedOrderRequest.setTradeType("JSAPI");
                wxPayUnifiedOrderRequest.setOpenid(openId);
//            wxPayUnifiedOrderRequest.setSignType("HMAC-SHA256");
//            wxPayUnifiedOrderRequest.setTimeExpire()
                try {
//                WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxService.unifiedOrder(wxPayUnifiedOrderRequest);
                    WxPayMpOrderResult wxPayMpOrderResult = wxPayService.createOrder(wxPayUnifiedOrderRequest);
                    respVO.setPkg(wxPayMpOrderResult.getPackageValue());
                    respVO.setAppId(wxPayMpOrderResult.getAppId());
                    respVO.setNonceStr(wxPayMpOrderResult.getNonceStr());
                    respVO.setPaySign(wxPayMpOrderResult.getPaySign());
                    respVO.setSignType("MD5");
                    respVO.setTimeStamp(wxPayMpOrderResult.getTimeStamp());
                } catch (WxPayException e) {
                    e.printStackTrace();
//                throw new RuntimeException(e);
                    throw exception(USER_WEIXIN_PAY_ERROR);
                }
                payOrderService.create(getLoginUserId(), orderNo, roomInfoDO.getStoreId(), "房间预定订单", price);
                //把订单号存到redis 如果已经验证了 就移除这个订单号
                redisTemplate.opsForSet().add(PAY_ORDER_REDIS_SET, orderNo);
            }
        }
        return respVO;
    }

    @Override
    public BigDecimal mathPrice(BigDecimal price, Date startTime, Date endTime, CouponInfoDO couponInfoDO) {
        long l = (endTime.getTime() - startTime.getTime()) / 1000 / 60;
        BigDecimal hour = BigDecimal.valueOf(l / 60.0);
        //计算价格 单价*时长
        BigDecimal totalPrice = price.multiply(hour);
        //判断使用优惠券的情况
        if (!ObjectUtils.isEmpty(couponInfoDO)) {
            //判断类型
            switch (couponInfoDO.getType()) {
                case 1://1抵扣券
                    //判断门槛
                    if (couponInfoDO.getMinUsePrice().compareTo(hour) > 0) {
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
                    if (couponInfoDO.getMinUsePrice().compareTo(totalPrice) > 0) {
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

    private String getRoomNameByType(Integer roomType) {
        if (roomType.compareTo(AppEnum.room_type.DA.getValue()) == 0) {
            return "大包";
        }
        if (roomType.compareTo(AppEnum.room_type.ZHONG.getValue()) == 0) {
            return "中包";
        }
        if (roomType.compareTo(AppEnum.room_type.XIAO.getValue()) == 0) {
            return "小包";
        }
        return "";
    }

    private void addPayRecord(Long storeId, BigDecimal price, Integer type, Integer moneyType, BigDecimal totalMoney, BigDecimal totalGiftMoney, String remark, Long userId) {
        UserMoneyBillDO userMoneyBillDO = new UserMoneyBillDO();
        userMoneyBillDO.setStoreId(storeId);
        userMoneyBillDO.setMoney(price);
        userMoneyBillDO.setType(type);
        userMoneyBillDO.setMoneyType(moneyType);
        userMoneyBillDO.setTotalMoney(totalMoney);
        userMoneyBillDO.setTotalGiftMoney(totalGiftMoney);
        userMoneyBillDO.setRemark(remark);
        userMoneyBillDO.setUserId(userId);
        userMoneyBillMapper.insert(userMoneyBillDO);
    }

    private boolean checkTongxiao(Date startTime, Date endTime) {
        return (startTime.getHours() >= 23 || startTime.getHours() < 4)
                && endTime.getHours() == 8 && endTime.getMinutes() == 0;
    }

    @Override
    @Transactional
    public Long save(OrderSaveReqVO reqVO) {
        Long userId = getLoginUserId();
        //二次检查 下单时时间是必须大于4小时的
        long l = (reqVO.getEndTime().getTime() - reqVO.getStartTime().getTime()) / 1000 / 60;
        if (l < 240) {
            throw exception(ORDER_TIME_MIN_ERROR);
        }
        //定义一些参数 备用
        String orderNo = reqVO.getOrderNo();
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
        BigDecimal groupPayPrice = BigDecimal.ZERO;
        BigDecimal oldPrice = BigDecimal.valueOf(l / 60.0).multiply(roomInfoDO.getPrice());//原价
        CouponInfoDO couponInfoDO = null;
        if (!ObjectUtils.isEmpty(reqVO.getCouponId())) {
            couponInfoDO = couponInfoMapper.selectById(reqVO.getCouponId());
        }
        //下单之前仍然再检查一遍 并计算出应付总金额
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(reqVO.getRoomId(), reqVO.getStartTime(), reqVO.getEndTime(), couponInfoDO, null, false);
        BigDecimal totalPrice = BigDecimal.valueOf(wxPayOrderRespVO.getPrice() / 100.0);
        //判断是否有填团购券
        if (!ObjectUtils.isEmpty(reqVO.getGroupPayNo())) {
            reqVO.setPayType(AppEnum.order_pay_type.TUANGOU.getValue());
            reqVO.setGroupPayNo(reqVO.getGroupPayNo().replaceAll(" ", ""));
            //校验团购券 先取出门店的美团配置信息
            StoreInfoDO storeInfoDO = storeInfoMapper.selectById(roomInfoDO.getStoreId());
            JSONObject chaxun = meituanService.prepare(storeInfoDO.getStoreId(), reqVO.getGroupPayNo());
            //套餐id，退款的时候要用
            String deal_id = chaxun.getStr("deal_id");
            //支付金额
            JSONObject paymentDetail = (JSONObject) chaxun.getJSONArray("payment_detail").get(0);
            groupPayPrice = paymentDetail.getBigDecimal("amount");
            //取出标题 并按|进行分割,格式为： 包间类型|自定义名称|时间 首位是包间类型，尾部是时间  如：大包|极品房间|4小时
            String dealTitle = chaxun.getStr("deal_title");
            //团购券的名称 如果包含 “通宵”两个字，说明是通宵场 23-8时
            if (dealTitle.indexOf("通宵") > 0) {
                //通宵场  判断开始时间必须大于23:00 小于4:00   结束时间必须等于08:00
                if (!checkTongxiao(reqVO.getStartTime(), reqVO.getEndTime())) {
                    throw exception(GROUP_NO_CHECK_TONGXIAO_TIME_ERROR);
                }
            } else {
                //普通券
                String[] split = new String[0];
                try {
                    split = dealTitle.split("\\|");
                    if (split.length < 3) {
                        throw exception(GROUP_NO_CHECK_ERROR);
                    }
                } catch (Exception e) {
//                    throw new RuntimeException(e);
                    throw exception(GROUP_NO_CHECK_ERROR);
                }
                String roomTypeName = split[0];
                Integer timeHour = Integer.valueOf(split[split.length - 1].replace("小时", ""));
                String roomNameByType = getRoomNameByType(roomInfoDO.getType());
                if (!roomNameByType.equals(roomTypeName)) {
                    throw exception(GOURP_NO_PAY_ROOM_TYPE_CHECK_ERROR);
                }
                if (l / 60 != timeHour) {
                    throw exception(GOURP_NO_PAY_TIME_HOUR_CHECK_ERROR);
                }
            }
            //检验通过  把团购券给使用了
            JSONObject consume = meituanService.consume(roomInfoDO.getStoreId(), userId, reqVO.getGroupPayNo());

            //记录下来
            reqVO.setGroupPayNo(reqVO.getGroupPayNo() + "-" + deal_id);
            //团购消费的  支付价格设置为0
            totalPrice = BigDecimal.ZERO;
        } else {
            //非团购支付 判断支付方式
            //判断使用优惠券的情况
            if (!ObjectUtils.isEmpty(reqVO.getCouponId())) {
                //判断使用状态
                if (ObjectUtils.isEmpty(couponInfoDO) || couponInfoDO.getStatus().intValue() != 0 || couponInfoDO.getExpriceTime().after(new Date())) {
                    exception(COUPON_USED_ERROR);
                }
                //判断限制门店
                if (!ObjectUtils.isEmpty(couponInfoDO.getStoreIds())) {
                    if (!couponInfoDO.getStoreIds().equals(String.valueOf(roomInfoDO.getStoreId()))) {
                        exception(COUPON_USE_CHECK_ERROR);
                    }
                    //通宵券 开始时间必须是大于23时  小于 4时 结束时间必须是8时
                    if (couponInfoDO.getCouponName().indexOf("通宵") != -1) {
                        if (!checkTongxiao(reqVO.getStartTime(), reqVO.getEndTime())) {
                            throw exception(TONGXIAO_COUPON_USE_ERROR);
                        }
                    }
                }
                //判断限制房间类型
//            if (!ObjectUtils.isEmpty(couponInfoDO.getRoomType())) {
//                if (roomInfoDO.getType() > couponInfoDO.getRoomType()) {
//                    exception(COUPON_USE_CHECK_ERROR);
//                }
//            }
                //use
                couponInfoDO.setStatus(AppEnum.coupon_status.USED.getValue());
                couponInfoMapper.updateById(couponInfoDO);
            }
            //订单价格为0  就不需要扣费了
            if (totalPrice.compareTo(BigDecimal.ZERO) > 0) {
                switch (reqVO.getPayType()) {
                    case 1://微信
                        if (ObjectUtils.isEmpty(reqVO.getOrderNo())) {
                            throw exception(ORDER_WEIXIN_PAY_ERROR);
                        }
                        // 从redis查询 存在的情况才处理，防止重复验证
                        if (redisTemplate.opsForSet().isMember(PAY_ORDER_REDIS_SET, reqVO.getOrderNo())) {
                            //有支付单号，再验证支付是否成功
                            PayOrderDO payOrderDO = payOrderService.getByOrderNo(reqVO.getOrderNo());
                            if (ObjectUtils.isEmpty(payOrderDO)) {
                                throw exception(ORDER_WEIXIN_PAY_ERROR);
                            } else if (!payOrderService.checkWxOrder(payOrderDO.getOrderNo(), wxPayOrderRespVO.getPrice())) {
                                throw exception(ORDER_WEIXIN_PAY_ERROR);
                            } else if (!payOrderDO.getPayStatus()) {
                                throw exception(ORDER_WEIXIN_PAY_ERROR);
                            }
                            //对比实际支付的价格 和订单应支付的价格是否一致
                            if (payOrderDO.getPrice().compareTo(wxPayOrderRespVO.getPrice()) != 0) {
                                throw exception(ORDER_WEIXIN_PAY_ERROR);
                            }
                            //如果已经验证成功了 就移除这个订单号
                            redisTemplate.opsForSet().remove(PAY_ORDER_REDIS_SET, reqVO.getOrderNo());
                        } else {
                            throw exception(ORDER_WEIXIN_PAY_ERROR);
                        }
                        //
                        break;
                    case 2://余额
                        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(userId, roomInfoDO.getStoreId());
                        if (ObjectUtils.isEmpty(storeUserDO)) {
                            //没有余额 报错余额不足
                            throw exception(MEMBER_BALANCE_MIN_ERROR);
                        }
                        //先扣钱包余额
                        if (storeUserDO.getBalance().compareTo(totalPrice) >= 0) {
                            //钱够 直接扣
                            storeUserDO.setBalance(storeUserDO.getBalance().subtract(totalPrice));
                            synchronized (this) {
                                storeUserMapper.updateById(storeUserDO);
                            }
                            //增加付款记录
                            addPayRecord(roomInfoDO.getStoreId(), totalPrice, AppEnum.user_money_bill_type.PAY.getValue(), 1, storeUserDO.getBalance(), null, "订单：" + orderNo + ",支付", userId);
                        } else {
                            //钱不够  看看有没有赠送余额 加起来判断够不够
                            BigDecimal userBalance = storeUserDO.getBalance();
                            BigDecimal added = storeUserDO.getBalance().add(storeUserDO.getGiftBalance());
                            //有 加起余额一起判断
                            if (added.compareTo(totalPrice) >= 0) {
                                //钱够 先扣完余额 再扣赠送余额
                                BigDecimal subtract = totalPrice.subtract(storeUserDO.getBalance());//要从赠送余额扣的钱
                                storeUserDO.setBalance(BigDecimal.ZERO);
                                storeUserDO.setGiftBalance(storeUserDO.getGiftBalance().subtract(subtract));
                                synchronized (this) {
                                    storeUserMapper.updateById(storeUserDO);
                                }
                                //增加付款记录
                                if (userBalance.compareTo(BigDecimal.ZERO) > 0) {
                                    addPayRecord(roomInfoDO.getStoreId(), userBalance, AppEnum.user_money_bill_type.PAY.getValue(), AppEnum.user_money_type.MONEY.getValue(), BigDecimal.ZERO, null, "订单：" + orderNo + ",支付", userId);
                                }
                                addPayRecord(roomInfoDO.getStoreId(), subtract, AppEnum.user_money_bill_type.PAY.getValue(), AppEnum.user_money_type.GIFT_MONEY.getValue(), null, storeUserDO.getGiftBalance(), "订单：" + orderNo + ",支付", userId);
                            } else {
                                throw exception(MEMBER_BALANCE_MIN_ERROR);
                            }
                        }
                        break;
                    default:
                        throw exception(PAY_TYPE_ERROR);
                }
            }
        }
        //生成订单，并修改房间状态
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        orderInfoDO.setOrderNo(reqVO.getOrderNo());
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
        orderInfoDO.setGroupPayPrice(groupPayPrice);
        orderInfoDO.setCouponId(reqVO.getCouponId());
        orderInfoMapper.insert(orderInfoDO);
        //如果房间状态不是进行中，就改成已预定
        if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.USED.getValue()) != 0) {
            roomInfoDO.setStatus(AppEnum.room_status.PENDDING.getValue());
            roomInfoMapper.updateById(roomInfoDO);
        }
        //异步发送微信通知
        workWxService.sendOrderMsg(roomInfoDO.getStoreId(), userId, roomInfoDO.getRoomName(), totalPrice, reqVO.getPayType(), orderNo, orderInfoDO.getStartTime(), orderInfoDO.getEndTime());
        return orderInfoDO.getOrderId();

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
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(orderInfoDO.getRoomId(), startTime, endTime, null, reqVO.getOrderId(), false);
        BigDecimal totalPrice = BigDecimal.valueOf(wxPayOrderRespVO.getPrice() / 100.0);
        switch (reqVO.getPayType()) {
            case 1://微信
                if (ObjectUtils.isEmpty(reqVO.getOrderNo())) {
                    throw exception(ORDER_WEIXIN_PAY_ERROR);
                }
                // 从redis查询 存在的情况才处理，防止重复验证充值
                if (redisTemplate.opsForSet().isMember(PAY_ORDER_REDIS_SET, reqVO.getOrderNo())) {
                    //有支付单号，再验证支付是否成功
                    PayOrderDO payOrderDO = payOrderService.getByOrderNo(reqVO.getOrderNo());
                    if (ObjectUtils.isEmpty(payOrderDO)) {
                        throw exception(ORDER_WEIXIN_PAY_ERROR);
                    } else if (!payOrderService.checkWxOrder(payOrderDO.getOrderNo(), wxPayOrderRespVO.getPrice())) {
                        throw exception(ORDER_WEIXIN_PAY_ERROR);
                    } else if (!payOrderDO.getPayStatus()) {
                        throw exception(ORDER_WEIXIN_PAY_ERROR);
                    }
                    //对比实际支付的价格 和订单应支付的价格是否一致
                    if (payOrderDO.getPrice().compareTo(wxPayOrderRespVO.getPrice()) != 0) {
                        throw exception(ORDER_WEIXIN_PAY_ERROR);
                    }
                    //如果已经验证成功了 就移除这个订单号
                    redisTemplate.opsForSet().remove(PAY_ORDER_REDIS_SET, reqVO.getOrderNo());
                    //是微信支付的  增加已支付的金额
                    orderInfoDO.setPayPrice(orderInfoDO.getPayPrice().add(totalPrice));
                } else {
                    throw exception(ORDER_WEIXIN_PAY_ERROR);
                }
                break;
            case 2://余额
                StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(userId, roomInfoDO.getStoreId());
                if (ObjectUtils.isEmpty(storeUserDO)) {
                    //没有余额 报错余额不足
                    throw exception(MEMBER_BALANCE_MIN_ERROR);
                }
                //先扣钱包余额
                if (storeUserDO.getBalance().compareTo(totalPrice) >= 0) {
                    //钱够 直接扣
                    storeUserDO.setBalance(storeUserDO.getBalance().subtract(totalPrice));
                    synchronized (this) {
                        storeUserMapper.updateById(storeUserDO);
                    }
                    //增加付款记录
                    addPayRecord(roomInfoDO.getStoreId(), totalPrice, AppEnum.user_money_bill_type.PAY.getValue(), 1, storeUserDO.getBalance(), null, "订单：" + orderInfoDO.getOrderNo() + ",续费", userId);
                } else {
                    //钱不够  看看有没有赠送余额 加起来判断够不够
                    BigDecimal userBalance = storeUserDO.getBalance();
                    //有 加起余额一起判断
                    BigDecimal added = storeUserDO.getBalance().add(storeUserDO.getGiftBalance());
                    if (added.compareTo(totalPrice) >= 0) {
                        //钱够 先扣赠送余额 再扣余额
                        BigDecimal subtract = totalPrice.subtract(storeUserDO.getBalance());//要从赠送余额扣的钱
                        storeUserDO.setBalance(BigDecimal.ZERO);
                        storeUserDO.setGiftBalance(storeUserDO.getGiftBalance().subtract(subtract));
                        synchronized (this) {
                            storeUserMapper.updateById(storeUserDO);
                        }
                        //增加付款记录
                        addPayRecord(roomInfoDO.getStoreId(), userBalance, AppEnum.user_money_bill_type.PAY.getValue(), 1, BigDecimal.ZERO, null, "订单：" + orderInfoDO.getOrderNo() + ",续费", userId);
                        addPayRecord(roomInfoDO.getStoreId(), subtract, AppEnum.user_money_bill_type.PAY.getValue(), 2, storeUserDO.getGiftBalance(), null, "订单：" + orderInfoDO.getOrderNo() + ",续费", userId);
                    } else {
                        throw exception(MEMBER_BALANCE_MIN_ERROR);
                    }
                }
                break;
            default:
                throw exception(PAY_TYPE_ERROR);
        }
        //支付完了，增加订单的结束时间
        orderInfoDO.setEndTime(DateUtils.addDate(orderInfoDO.getEndTime(), Calendar.MINUTE, reqVO.getMinutes()));
        //增加订单金额
        orderInfoDO.setPrice(orderInfoDO.getPrice().add(oldPrice));
        //如果状态是已完成，则状态改成进行中 并触发一次开房间门操作，以实现通电
        if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.FINISH.getValue()) == 0) {
            orderInfoDO.setStatus(AppEnum.order_status.START.getValue());
            deviceService.openRoomDoor(roomInfoDO.getRoomId(), orderInfoDO.getOrderId(), 1);
            if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.USED.getValue()) != 0) {
                roomInfoDO.setStatus(AppEnum.room_status.USED.getValue());
                roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), roomInfoDO.getRoomId());
            }
        }
        orderInfoMapper.updateById(orderInfoDO);
        //异步发送微信通知
        workWxService.sendRenewMsg(roomInfoDO.getStoreId(), userId, roomInfoDO.getRoomName(), totalPrice, reqVO.getPayType(),
                orderInfoDO.getOrderNo(), orderInfoDO.getEndTime(), false);
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
        //如果没有传订单id 就返回该用户最近的一笔订单  endTime>now
        OrderInfoAppRespVO orderInfo = orderInfoMapper.getOrderInfo(orderId, getLoginUserId());
        if (ObjectUtils.isEmpty(orderInfo)) {
            throw exception(ORDER_NOT_FOUND_ERROR);
        }
        return orderInfo;
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
                //改旧房间的状态  如果房间没有其他订单，则改回空闲
//                if (roomInfoDO1.getStatus().compareTo(AppEnum.room_status.PENDDING.getValue()) == 0) {
                if (orderInfoMapper.countByRoomId(roomInfoDO1.getRoomId(), orderId) > 0) {
                    roomInfoDO1.setStatus(AppEnum.room_status.PENDDING.getValue());
                    roomInfoMapper.updateById(roomInfoDO1);
                } else {
                    roomInfoDO1.setStatus(AppEnum.room_status.ENABLE.getValue());
                    roomInfoMapper.updateById(roomInfoDO1);
                }
//                }
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
        boolean cancelFlag = true;//默认允许取消订单
        //对于用户  只能取消自己的订单
        if (orderInfoDO.getUserId().compareTo(loginUserId) != 0) {
            throw exception(OPRATION_ERROR);
        }
        //未开始和进行中的订单  并且订单创建时间在5分钟内  都可以取消  其他则不能
        cancelFlag = orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0 || orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0;
        LocalDateTime currentDateTime = LocalDateTime.now(); // 当前时间
        LocalDateTime fiveMinutesAfter = currentDateTime.plus(5, ChronoUnit.MINUTES); // 当前时间5分钟后的时间
        if (orderInfoDO.getCreateTime().isAfter(fiveMinutesAfter)) {
            //订单创建时间超过了当前时间5分钟
            cancelFlag = false;
        }
        if (cancelFlag) {
            //判断支付方式
            if (!ObjectUtils.isEmpty(orderInfoDO.getGroupPayNo())) {
                String[] split = orderInfoDO.getGroupPayNo().split("-");//团购码在前   deal_id在后
                JSONObject reverseconsume = meituanService.reverseconsume(orderInfoDO.getStoreId(), orderInfoDO.getUserId(), split[0], split[1]);
            } else {
                //实际支付金额为0  就不退款了
                if (orderInfoDO.getPayPrice().compareTo(BigDecimal.ZERO) > 0) {
                    if (orderInfoDO.getPayType().compareTo(AppEnum.order_pay_type.WEIXIN.getValue()) == 0) {
                        //微信退款
                        PayOrderDO payOrderDO = payOrderMapper.getByOrderNo(orderInfoDO.getOrderNo());
                        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
                        refundRequest.setOutTradeNo(orderInfoDO.getOrderNo());
                        refundRequest.setOutRefundNo("TK" + orderInfoDO.getOrderNo());
                        refundRequest.setTotalFee(payOrderDO.getPrice());
                        refundRequest.setRefundFee(payOrderDO.getPrice());
                        try {
                            wxPayService.refund(refundRequest);
                            payOrderDO.setPayRefundNo(refundRequest.getOutRefundNo());
                            payOrderDO.setRefundPrice(payOrderDO.getPrice());
                            payOrderDO.setRefundTime(LocalDateTime.now());
                            payOrderMapper.updateById(payOrderDO);
                        } catch (WxPayException e) {
                            e.printStackTrace();
//                        throw new RuntimeException(e);
                            throw exception(USER_WEIXIN_PAY_REFUND_ERROR);
                        }
                    } else {
                        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(loginUserId, orderInfoDO.getStoreId());
                        //余额退款  把支付记录找出来
                        List<UserMoneyBillDO> userMoneyBillDOList = userMoneyBillMapper.getPayByOrderNo(orderInfoDO.getOrderNo(), orderInfoDO.getUserId());
                        if (!org.springframework.util.CollectionUtils.isEmpty(userMoneyBillDOList)) {
                            for (UserMoneyBillDO billDO : userMoneyBillDOList) {
                                UserMoneyBillDO newUserMoneyBillDO = new UserMoneyBillDO();
                                BeanUtils.copyProperties(billDO, newUserMoneyBillDO);
                                newUserMoneyBillDO.setId(null);
                                newUserMoneyBillDO.setCreateTime(null);
                                newUserMoneyBillDO.setCreator(null);
                                newUserMoneyBillDO.setUpdateTime(null);
                                newUserMoneyBillDO.setUpdater(null);
                                newUserMoneyBillDO.setType(AppEnum.user_money_bill_type.REFUND.getValue());//改成退款状态
                                newUserMoneyBillDO.setRemark(newUserMoneyBillDO.getRemark().replace("支付", "退款"));
                                if (billDO.getMoneyType().intValue() == 1) {
                                    //账户余额  加回去
                                    storeUserDO.setBalance(storeUserDO.getBalance().add(billDO.getMoney()));
                                    synchronized (this) {
                                        storeUserMapper.updateById(storeUserDO);
                                    }
                                    newUserMoneyBillDO.setTotalMoney(storeUserDO.getBalance());
                                } else if (billDO.getMoneyType().intValue() == 2) {
                                    //赠送余额  加回去
                                    storeUserDO.setGiftBalance(storeUserDO.getGiftBalance().add(billDO.getMoney()));
                                    synchronized (this) {
                                        storeUserMapper.updateById(storeUserDO);
                                    }
                                    newUserMoneyBillDO.setTotalGiftMoney(storeUserDO.getGiftBalance());
                                } else {
                                    throw exception(OPRATION_ERROR);
                                }
                                userMoneyBillMapper.insert(newUserMoneyBillDO);
                            }
                        }
                    }
                }
                //退还优惠券
                if (!ObjectUtils.isEmpty(orderInfoDO.getCouponId())) {
                    CouponInfoDO couponInfoDO = couponInfoMapper.selectById(orderInfoDO.getCouponId());
                    if (couponInfoDO.getExpriceTime().after(new Date())) {
                        couponInfoDO.setStatus(AppEnum.coupon_status.AVAILABLE.getValue());
                        couponInfoMapper.updateById(couponInfoDO);
                    }

                }
                orderInfoDO.setRefundPrice(orderInfoDO.getPayPrice());
            }
            //取消的订单已开始  那就触发一下关门
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                deviceService.closeRoomDoor(orderInfoDO.getRoomId(), null, 4);
            }
            //设置订单状态为取消
            orderInfoDO.setStatus(AppEnum.order_status.CANCEL.getValue());
            //取消后  如果后面没有预约了，把房间状态改回空闲
            if (orderInfoMapper.countByRoomId(orderInfoDO.getRoomId(), orderId) > 0) {
                roomInfoMapper.updateStatusById(AppEnum.room_status.PENDDING.getValue(), orderInfoDO.getRoomId());
            } else {
                roomInfoMapper.updateStatusById(AppEnum.room_status.ENABLE.getValue(), orderInfoDO.getRoomId());
            }
            orderInfoMapper.updateById(orderInfoDO);
            //异步发送微信通知
            workWxService.sendOrderCancelMsg(orderInfoDO.getStoreId(), loginUserId, orderInfoDO.getRoomId(), orderInfoDO.getPayPrice(), orderInfoDO.getPayType(), orderInfoDO.getOrderNo());
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
                //对于通宵场，开始时间只能在23时以后 4时之前
                if (checkTongxiao(orderInfoDO.getStartTime(), orderInfoDO.getEndTime())) {
                    //通宵场
                    //判断是否具备提前开始的条件，23点及以后 或者4点以前
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 8); // 设置小时为8
                    calendar.set(Calendar.MINUTE, 0); // 设置分钟为0
                    calendar.set(Calendar.SECOND, 0); // 设置秒钟为0
                    calendar.set(Calendar.MILLISECOND, 0); // 设置毫秒为0
                    if (now.getHours() >= 23) {
                        //今日23时之后开始 开始时间就等于现在  结束时间等于次日8时
                        calendar.add(Calendar.DAY_OF_MONTH, 1); // 加一天
                        orderInfoDO.setEndTime(calendar.getTime());
                    } else if (now.getHours() < 4) {
                        //次日4点以前开始 开始时间就等于现在  结束时间等于今日8时
                        orderInfoDO.setEndTime(calendar.getTime());
                    } else {
                        throw exception(TONGXIAO_ORDER_START_ERROR);
                    }
                } else {
                    //新的结束时间 等于当前时间加上订单的时长
                    long l = now.getTime() + (orderInfoDO.getEndTime().getTime() - orderInfoDO.getStartTime().getTime());
                    Date endTime = new Date(l);
                    //更改订单的开始和完成时间
                    orderInfoDO.setEndTime(endTime);
                    log.info("订单：{}，提前开始消费！", orderInfoDO.getOrderNo());
                }
                orderInfoDO.setStartTime(now);
                //校验时间冲突
                preOrder(orderInfoDO.getRoomId(), now, orderInfoDO.getEndTime(), null, orderId, false);
            }
            //开始订单
            orderInfoDO.setStatus(AppEnum.order_status.START.getValue());
            orderInfoMapper.updateById(orderInfoDO);
            //房间改为进行中
            roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), orderInfoDO.getRoomId());
            //将该房间历史的保洁订单，未开始的  改成取消
            clearInfoMapper.cancelByRoomId(orderInfoDO.getRoomId());
            //开门开电
            deviceService.openRoomDoor(orderInfoDO.getRoomId(), null, 4);
            //播放欢迎语
//            deviceService.runSound(orderInfoDO.getRoomId(), 4);
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
        log.info("==========     开始执行订单定时检查任务     ==========");
        Date now = new Date();
        log.info("当前时间:{}", DateUtils.dateToStr(now, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
        //取出所有进行中的订单
        List<OrderInfoDO> listStart = orderInfoMapper.getByStatus(AppEnum.order_status.START.getValue());
        //如果存在结束时间已经小于现在的时间的 则把订单状态改为完成
        if (!org.springframework.util.CollectionUtils.isEmpty(listStart)) {
            List<String> roomIds = new ArrayList<>();
            Set<String> storeIds = new HashSet<>();
            List<String> orderIds = new ArrayList<>();
            //新增保洁订单
            List<ClearInfoDO> clearInfoDOList = new ArrayList<>();
            listStart.forEach(x -> {
                log.info("进行中订单：{}，结束时间:{}", x.getOrderNo(), DateUtils.dateToStr(x.getEndTime(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
                //进行中订单的结束时间 小于当前时间 则结束订单
                if (x.getEndTime().before(now)) {
                    log.info("结束订单：{}", x.getOrderNo());
//                    x.setStatus(AppEnum.order_status.FINISH.getValue());
                    orderIds.add(String.valueOf(x.getOrderId()));
                    //房间改为待保洁
                    roomIds.add(String.valueOf(x.getRoomId()));
                    //新增保洁订单
                    ClearInfoDO clearInfoDO = new ClearInfoDO();
                    clearInfoDO.setOrderId(x.getOrderId());
                    clearInfoDO.setStoreId(x.getStoreId());
                    clearInfoDO.setOrderNo(x.getOrderNo());
                    clearInfoDO.setRoomId(x.getRoomId());
                    clearInfoDOList.add(clearInfoDO);
                    //关门关电
                    deviceService.closeRoomDoor(x.getRoomId(), null, 4);
                    storeIds.add(x.getStoreId().toString());
                } else {
                    //如果订单结束时间  还剩30分钟，发送提醒
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(now);
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(x.getEndTime());
                    // 忽略秒
                    cal1.set(Calendar.SECOND, 0);
                    cal2.set(Calendar.SECOND, 0);
                    long milliseconds1 = cal1.getTimeInMillis();
                    long milliseconds2 = cal2.getTimeInMillis();
                    long diff = milliseconds2 - milliseconds1;
                    int minutes = (int) (diff / (60 * 1000));
                    if (minutes == 30) {
                        deviceService.runSound(x.getRoomId(), 2);
                    } else if (minutes == 15) {
                        deviceService.runSound(x.getRoomId(), 3);
                    } else if (minutes == 5) {
                        deviceService.runSound(x.getRoomId(), 4);
                    }
                }
            });
            if (!org.springframework.util.CollectionUtils.isEmpty(roomIds)) {
                orderInfoMapper.updateStatusByIds(AppEnum.order_status.FINISH.getValue(), orderIds.stream().collect(Collectors.joining(",")));
                roomInfoMapper.updateStatusByIds(AppEnum.room_status.CLEAR.getValue(), roomIds.stream().collect(Collectors.joining(",")));
                clearInfoMapper.insertBatch(clearInfoDOList);
                //发送微信通知
                sendClearMsg(roomIds, storeIds);
            }
        }
        //取出所有未开始的订单
        List<OrderInfoDO> list1 = orderInfoMapper.getByStatus(AppEnum.order_status.PENDING.getValue());
        //如果存在开始时间已经大于现在的时间的 则把订单状态改为开始
        if (!org.springframework.util.CollectionUtils.isEmpty(list1)) {
            List<String> roomIds = new ArrayList<>();
            List<String> orderIds = new ArrayList<>();
            list1.forEach(x -> {
                log.info("未开始订单：{}，开始时间:{}", x.getOrderNo(), DateUtils.dateToStr(x.getStartTime(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
                //开始时间 小于 当前的时间，则开始订单
                if (x.getStartTime().before(now)) {
                    //开始订单
                    orderIds.add(String.valueOf(x.getOrderId()));
                    log.info("开始订单：{}", x.getOrderNo());
//                    x.setStatus(AppEnum.order_status.START.getValue());
                    //房间改为进行中
                    roomIds.add(String.valueOf(x.getRoomId()));
                }
            });
//            orderInfoMapper.updateBatch(list1);
            if (!org.springframework.util.CollectionUtils.isEmpty(roomIds)) {
                orderInfoMapper.updateStatusByIds(AppEnum.order_status.START.getValue(), orderIds.stream().collect(Collectors.joining(",")));
                roomInfoMapper.updateStatusByIds(AppEnum.room_status.USED.getValue(), roomIds.stream().collect(Collectors.joining(",")));
                //取消掉存在的保洁订单
                clearInfoMapper.cancelByRoomIds(roomIds.stream().collect(Collectors.joining(",")));
            }
        }
        log.info("==========     订单定时检查任务执行完成     ==========");

    }


    @Async
    protected void sendClearMsg(List<String> roomIds, Set<String> storeIds) {
        String dateStr = DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
        //先查询出所有门店 并转map
        Map<String, StoreInfoDO> storeInfoDOMap = storeInfoMapper.getListByIds(storeIds).stream().collect(Collectors.toMap(x -> String.valueOf(x.getStoreId()), Function.identity()));
        //开始发消息
        for (String roomId : roomIds) {
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
            StringBuffer sb = new StringBuffer();
            sb.append("订单结束,待清洁通知\n");
            sb.append(">门店名称:").append(storeInfoDOMap.get(roomInfoDO.getStoreId().toString()).getStoreName()).append("\n");
            sb.append(">房间名称:").append(roomInfoDO.getRoomName()).append("\n");
            sb.append(">时间:").append(dateStr).append("\n");
            workWxService.sendClearMsg(storeInfoDOMap.get(roomInfoDO.getStoreId().toString()).getOrderWebhook(), sb.toString());
        }
    }

    @Override
    @Transactional
    public boolean queryWxOrder(String orderNo) {
        return payOrderService.checkWxOrder(orderNo, null);
    }


    @Override
    public List<AppDiscountRulesRespVO> getDiscountRules(Long storeId) {
        return discountRulesMapper.getDiscountRulesByStoreId(storeId);
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
