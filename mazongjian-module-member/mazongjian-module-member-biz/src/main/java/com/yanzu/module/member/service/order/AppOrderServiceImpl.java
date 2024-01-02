package com.yanzu.module.member.service.order;

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
import com.yanzu.framework.tenant.core.context.TenantContextHolder;
import com.yanzu.module.member.controller.app.order.vo.*;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.dal.dataobject.storemeituaninfo.StoreMeituanInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.couponinfo.CouponInfoMapper;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;
import com.yanzu.module.member.dal.mysql.groupPay.GroupPayInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.payorder.PayOrderMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.dal.mysql.storemeituaninfo.StoreMeituanInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.AppUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.douyin.DouyinService;
import com.yanzu.module.member.service.douyin.vo.DouyinCancelReqVO;
import com.yanzu.module.member.service.douyin.vo.DouyinPrepareRespVO;
import com.yanzu.module.member.service.iot.EwlService;
import com.yanzu.module.member.service.meituan.MeituanService;
import com.yanzu.module.member.service.meituan.vo.MeituanPrepareRespVO;
import com.yanzu.module.member.service.payorder.PayOrderService;
import com.yanzu.module.member.service.wx.MyWxPayService;
import com.yanzu.module.member.service.wx.WorkWxService;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.module.member.enums.AppEnum.WX_PAY_ORDER;
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
    private MyWxPayService myWxPayService;

    @Resource
    private MeituanService meituanService;

    @Resource
    private DouyinService douyinService;

    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private DiscountRulesMapper discountRulesMapper;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private GroupPayInfoMapper groupPayInfoMapper;

    @Resource
    private WorkWxService workWxService;

    @Resource
    private EwlService ewlService;


    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private StoreMeituanInfoMapper storeMeituanInfoMapper;

    @Value("${wx.pay.returnUrl}")
    private String returnUrl;

    /**
     * @param roomId        房间id
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param couponInfoDO  优惠券
     * @param ignoreOrderId 忽略校验的订单id，用于更换房间 或者提前开始订单
     * @param nightLong     是否通宵
     * @param wxpay         是否微信下单
     * @return
     */
    @Override
    public WxPayOrderRespVO preOrder(Long roomId, Date startTime, Date endTime, CouponInfoDO couponInfoDO, Long ignoreOrderId, boolean nightLong, boolean wxpay) {
        Date now = new Date();
        //参数校验
        //开始时间不能小于结束时间
        if (startTime.after(endTime)) {
            throw exception(ORDER_START_TIME_GT_END_ERROR);
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
        //通宵场判断
        if (nightLong) {
            //通宵
            //判断订单时间是否合法  通宵场的开始时间必须大于23:00小于04:00  结束时间必须等于08:00  时间差不能大于9小时
            if (!checkTongxiao(startTime, endTime)) {
                throw exception(CHECK_TONGXIAO_TIME_ERROR);
            }
        } else {
            //非通宵
            if (startTime.before(now)) {
                //开始时间在当前之前，不能超过5分钟  不然间隔太久了
                long l = (now.getTime() - startTime.getTime()) / 1000 / 60;
                if (l > 6) {
                    throw exception(ORDER_START_TIME_LT_NOW_ERROR);
                }
            }
        }
        //查询出房间信息
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        //检查优惠券是否允许使用
        checkCouponUse(couponInfoDO, nightLong, roomInfoDO.getType(), roomInfoDO.getStoreId(), startTime, endTime);
        //计算订单价格
        BigDecimal mathPrice = mathPrice(roomInfoDO.getPrice(), startTime, endTime, couponInfoDO);
        //查询出该房间 所有的订单 以及不可用的时间段
        List<OrderInfoDO> orderInfoList = orderInfoMapper.getByRoomId(roomId, ignoreOrderId);
        //构建出不可用的时间区间
        List<TimeRange> disabledTimeRanges = new ArrayList<>();
        //先把订单中的时间进行处理
        if (!CollectionUtils.isAnyEmpty(orderInfoList)) {
            for (OrderInfoDO orderInfoDO : orderInfoList) {
                disabledTimeRanges.add(new TimeRange(DateUtils.of(orderInfoDO.getStartTime()), DateUtils.of(orderInfoDO.getEndTime())));
            }
        }
        //再处理每天有禁用时间的情况
        //获取当前日期
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
                if (startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(timeRange.getEnd()) && timeRange.getStart().isBefore(endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
                    //存在交集
                    throw exception(ORDER_TIME_CHECK_ERROR);
                }
            }
        }
        //随机生成一个订单号
        String orderNo = getOrderNo();
        //价格转成分为单位 微信支付使用
        int price = mathPrice.multiply(BigDecimal.valueOf(100D)).intValue();
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
                //创建微信支付实例
                WxPayService wxPayService = myWxPayService.init(roomInfoDO.getStoreId());
                //生成微信支付的订单
                WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
                wxPayUnifiedOrderRequest.setBody("微信支付订单");
                wxPayUnifiedOrderRequest.setOutTradeNo(orderNo);
                wxPayUnifiedOrderRequest.setTotalFee(price);
                wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
                wxPayUnifiedOrderRequest.setNotifyUrl(returnUrl);
                wxPayUnifiedOrderRequest.setTradeType("JSAPI");
                wxPayUnifiedOrderRequest.setProfitSharing(myWxPayService.getSplitEnable() ? "Y" : "N");
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
                if (null == ignoreOrderId) {
                    payOrderService.create(getLoginUserId(), orderNo, roomInfoDO.getStoreId(), "房间预定订单", price);
                } else {
                    payOrderService.create(getLoginUserId(), orderNo, roomInfoDO.getStoreId(), "续费订单", price);
                }
                //把这个信息存储到redis，在回调处验证后删除 最长1天过期
                WxPayOrderInfo wxPayOrderInfo = new WxPayOrderInfo(orderNo, getLoginUserId(), TenantContextHolder.getTenantId(), roomInfoDO.getStoreId(), roomId, startTime, endTime, ObjectUtils.isEmpty(couponInfoDO) ? null : couponInfoDO.getCouponId(), ignoreOrderId, price, nightLong);
                redisTemplate.opsForValue().set(String.format(WX_PAY_ORDER, orderNo), wxPayOrderInfo, 1, TimeUnit.DAYS);
            }
        }
        return respVO;
    }

    /**
     * 检查优惠券使用条件
     *
     * @param couponInfoDO
     * @param nightLong
     * @param roomType
     * @param startTime
     * @param endTime
     */
    private void checkCouponUse(CouponInfoDO couponInfoDO, boolean nightLong, Integer roomType, Long storeId, Date startTime, Date endTime) {
        //有使用优惠券再判断
        if (!ObjectUtils.isEmpty(couponInfoDO)) {
            if (couponInfoDO.getStatus().intValue() != 0 || couponInfoDO.getExpriceTime().after(new Date())) {
                exception(COUPON_USED_ERROR);
            }
            //判断适用门店
            if (couponInfoDO.getStoreId().compareTo(storeId) != 0) {
                exception(COUPON_USE_CHECK_STORE_ERROR);
            }
            //优惠券有限制的房间类型的  就判断房间类型
            if (!ObjectUtils.isEmpty(couponInfoDO) && !ObjectUtils.isEmpty(couponInfoDO.getRoomType())) {
                if (couponInfoDO.getRoomType().compareTo(roomType) != 0) {
                    throw exception(COUPON_USE_CHECK_ERROR);
                }
            }
            //普通场 不能使用通宵券
            if (!nightLong) {
                if (couponInfoDO.getCouponName().indexOf("通宵") != -1) {
                    throw exception(TONGXIAO_COUPON_USE_ERROR);
                }
            }

        }

    }


    @Override
    public BigDecimal mathPrice(BigDecimal price, Date startTime, Date endTime, CouponInfoDO couponInfoDO) {
        // 将秒字段设置为0，保持其他字段不变
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startTime);
        cal1.set(Calendar.SECOND, 0);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endTime);
        cal2.set(Calendar.SECOND, 0);
        // 计算两个日期的分钟差值
        long diff = Math.abs(cal2.getTimeInMillis() - cal1.getTimeInMillis());
        long minutes = diff / (60 * 1000);
        BigDecimal hour = new BigDecimal(String.valueOf(minutes / 60.0));
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
                        hour = hour.subtract(couponInfoDO.getPrice());
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
        return (startTime.getHours() >= 23 || startTime.getHours() < 4) && endTime.getHours() == 8 && endTime.getMinutes() == 0 && endTime.getTime() - startTime.getTime() <= 9 * 60 * 60 * 1000;
    }

    /**
     * 团购券合法性检查
     *
     * @param title
     * @param startTime
     * @param endTime
     * @param roomType
     * @param nightLong
     */
    private void checkGroupNo(String title, Date startTime, Date endTime, Integer roomType, boolean nightLong) {
        if (nightLong) {
            //通宵场 要求团购券必须包含 “通宵”两个字
            if (title.indexOf("通宵") == -1) {
                throw exception(GOURP_NO_PAY_TIME_HOUR_CHECK_ERROR);
            }
            //通宵场  判断开始时间必须大于23:00 小于4:00   结束时间必须等于08:00
            if (!checkTongxiao(startTime, endTime)) {
                throw exception(CHECK_TONGXIAO_TIME_ERROR);
            }
        } else {
            //非通宵
            //判断工作日限制情况  标题包含工作日和周一 就视为工作日券
            if (title.indexOf("工作日") != -1 || title.indexOf("周一") != -1) {
                //仅工作日周一 - 周四可用
                checkWorkDay(startTime);
            }
            //判断包间限制情况  标题包含：不限包间
            if (title.indexOf("不限包间") != -1) {
                //不校验
            } else {
                Integer checkRoomType = 0;
                if (title.indexOf("大包") != -1) {
                    //大包
                    checkRoomType = AppEnum.room_type.DA.getValue();
                } else if (title.indexOf("中包") != -1) {
                    //中包
                    checkRoomType = AppEnum.room_type.ZHONG.getValue();
                } else if (title.indexOf("小包") != -1) {
                    //小包
                    checkRoomType = AppEnum.room_type.XIAO.getValue();
                } else {
                    //一个都没匹配上  那就默认小包
                    checkRoomType = AppEnum.room_type.XIAO.getValue();
                }
                if (roomType.compareTo(checkRoomType) != 0) {
                    throw exception(GOURP_NO_PAY_ROOM_TYPE_CHECK_ERROR);
                }
            }
            //判断时长是否匹配
            int timeIndex = title.indexOf("小时");
            int timeHour = 0;
            if (timeIndex == -1) {
                //没找到 默认4小时
                timeHour = 4;
            } else {
                //找到了 取时间
                String timeStr = title.substring(timeIndex - 1, timeIndex);
                timeHour = Integer.valueOf(timeStr);
            }
            long l = (endTime.getTime() - startTime.getTime()) / 1000 / 60;
            if (l / 60 != timeHour) {
                throw exception(GOURP_NO_PAY_TIME_HOUR_CHECK_ERROR);
            }
        }
    }

    private void checkWorkDay(Date startTime) {
        Calendar sc = Calendar.getInstance();
        sc.setTime(startTime);
        int scDayOfWeek = sc.get(Calendar.DAY_OF_WEEK);
        switch (scDayOfWeek) {
            case Calendar.SUNDAY:
            case Calendar.FRIDAY:
            case Calendar.SATURDAY:
                throw exception(GROUP_PAY_WORK_CHECK_ERROR);
        }
    }

    @Override
    @Transactional
    public Long save(OrderSaveReqVO reqVO) {
        if (ObjectUtils.isEmpty(reqVO.getUserId())) {
            reqVO.setUserId(getLoginUserId());
        }
        //二次检查 下单时时间是必须大于4小时的
        long l = (reqVO.getEndTime().getTime() - reqVO.getStartTime().getTime()) / 1000 / 60;
        if (l < 240) {
            throw exception(ORDER_TIME_MIN_ERROR);
        }
        //定义一些参数 备用
        String orderNo = reqVO.getOrderNo();
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
        //原价  就是每小时单价 * 时间
        BigDecimal oldPrice = BigDecimal.valueOf(l / 60.0).multiply(roomInfoDO.getPrice());
        CouponInfoDO couponInfoDO = null;
        if (!ObjectUtils.isEmpty(reqVO.getCouponId())) {
            couponInfoDO = couponInfoMapper.selectById(reqVO.getCouponId());
        }
        //下单之前仍然再检查一遍 并计算出应付总金额
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(reqVO.getRoomId(), reqVO.getStartTime(), reqVO.getEndTime(), couponInfoDO, null, reqVO.getNightLong(), false);
        BigDecimal totalPrice = new BigDecimal(String.valueOf(wxPayOrderRespVO.getPrice() / 100.0));
        //判断是否有填团购券  先预声明一些团购要的字段
        String groupName = "";
        String groupShopId = "";
        String groupNo = "";
        Integer groupType = null;
        BigDecimal groupPrice = BigDecimal.ZERO;
        if (!ObjectUtils.isEmpty(reqVO.getGroupPayNo())) {
            //设置订单的支付类型为团购
            reqVO.setPayType(AppEnum.order_pay_type.TUANGOU.getValue());
            //处理掉中间有空格的情况
            reqVO.setGroupPayNo(reqVO.getGroupPayNo().replaceAll(" ", ""));
            //判断是抖音券还是美团券
            if (reqVO.getGroupPayNo().length() <= 12) {
                //美团券
                groupType = AppEnum.member_group_no_type.MEITUAN.getValue();
                //查询券信息
                MeituanPrepareRespVO prepare = meituanService.prepare(roomInfoDO.getStoreId(), reqVO.getGroupPayNo());
                groupName = prepare.getTitle();
                groupNo = reqVO.getGroupPayNo();
                groupPrice = prepare.getPayAmount();
                groupShopId = prepare.getDealId();
                checkGroupNo(prepare.getTitle(), reqVO.getStartTime(), reqVO.getEndTime(), roomInfoDO.getType(), reqVO.getNightLong());
                //检验通过  把团购券给使用了
                meituanService.consume(roomInfoDO.getStoreId(), reqVO.getUserId(), reqVO.getGroupPayNo());
            } else {
                //抖音券
                groupType = AppEnum.member_group_no_type.DOUYIN.getValue();
                DouyinPrepareRespVO prepare = douyinService.prepare(reqVO.getGroupPayNo());
                groupName = prepare.getTitle();
                groupPrice = new BigDecimal(String.valueOf(prepare.getPayAmount() / 100.0));
                checkGroupNo(prepare.getTitle(), reqVO.getStartTime(), reqVO.getEndTime(), roomInfoDO.getType(), reqVO.getNightLong());
                //检验通过  把团购券给使用了
                String verify = douyinService.verify(roomInfoDO.getStoreId(), reqVO.getUserId(), prepare);
                groupNo = verify;
            }
            //团购消费的  支付价格设置为0
            totalPrice = BigDecimal.ZERO;
        } else {
            //非团购支付 判断支付方式
            //判断使用优惠券的情况
            if (!ObjectUtils.isEmpty(reqVO.getCouponId())) {
                //use
                couponInfoDO.setStatus(AppEnum.coupon_status.USED.getValue());
                couponInfoMapper.updateById(couponInfoDO);
            }
            //订单价格为0  就不需要扣费了
            if (totalPrice.compareTo(BigDecimal.ZERO) > 0) {
                switch (reqVO.getPayType()) {
                    case 1:
                        //微信
                        // 从redis查询 存在的情况才处理，防止重复验证
                        String redisKey = String.format(WX_PAY_ORDER, reqVO.getOrderNo());
                        if (redisTemplate.hasKey(redisKey)) {
                            //如果已经验证了 就移除这个订单号
                            redisTemplate.delete(redisKey);
                            //有支付单号，再验证支付是否成功
                            PayOrderDO payOrderDO = payOrderService.getByOrderNo(reqVO.getOrderNo());
                            if (ObjectUtils.isEmpty(payOrderDO)) {
                                throw exception(ORDER_WEIXIN_PAY_ERROR);
                            } else if (!payOrderService.checkWxOrder(payOrderDO.getOrderNo(), payOrderDO.getStoreId(), wxPayOrderRespVO.getPrice())) {
                                throw exception(ORDER_WEIXIN_PAY_ERROR);
                            } else if (!payOrderDO.getPayStatus()) {
                                throw exception(ORDER_WEIXIN_PAY_ERROR);
                            }
                        } else {
                            throw exception(ORDER_WEIXIN_PAY_ERROR);
                        }
                        break;
                    case 2://余额
                        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(reqVO.getUserId(), roomInfoDO.getStoreId());
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
                            addPayRecord(roomInfoDO.getStoreId(), totalPrice, AppEnum.user_money_bill_type.PAY.getValue(), 1, storeUserDO.getBalance(), null, "订单：" + orderNo + ",支付", reqVO.getUserId());
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
                                    addPayRecord(roomInfoDO.getStoreId(), userBalance, AppEnum.user_money_bill_type.PAY.getValue(), AppEnum.user_money_type.MONEY.getValue(), BigDecimal.ZERO, null, "订单：" + orderNo + ",支付", reqVO.getUserId());
                                }
                                addPayRecord(roomInfoDO.getStoreId(), subtract, AppEnum.user_money_bill_type.PAY.getValue(), AppEnum.user_money_type.GIFT_MONEY.getValue(), null, storeUserDO.getGiftBalance(), "订单：" + orderNo + ",支付", reqVO.getUserId());
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
        orderInfoDO.setOrderNo(reqVO.getOrderNo());
        orderInfoDO.setStoreId(roomInfoDO.getStoreId());
        orderInfoDO.setRoomId(roomInfoDO.getRoomId());
        orderInfoDO.setUserId(reqVO.getUserId());
        orderInfoDO.setStartTime(reqVO.getStartTime());
        orderInfoDO.setEndTime(reqVO.getEndTime());
        orderInfoDO.setNightLong(reqVO.getNightLong());
        orderInfoDO.setPrice(oldPrice);
        orderInfoDO.setPayPrice(totalPrice);
        orderInfoDO.setRefundPrice(BigDecimal.ZERO);
        orderInfoDO.setPayType(reqVO.getPayType());
        orderInfoDO.setGroupPayNo(reqVO.getGroupPayNo());
        orderInfoDO.setGroupPayType(groupType);
        orderInfoDO.setCouponId(reqVO.getCouponId());
        orderInfoMapper.insert(orderInfoDO);
        //如果房间状态不是进行中，就改成已预定
        if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.USED.getValue()) != 0) {
            roomInfoDO.setStatus(AppEnum.room_status.PENDDING.getValue());
            roomInfoMapper.updateById(roomInfoDO);
        }
        //如果使用了团购券 就增加团购验券记录
        if (!ObjectUtils.isEmpty(reqVO.getGroupPayNo())) {
            GroupPayInfoDO groupPayInfoDO = new GroupPayInfoDO();
            groupPayInfoDO.setGroupName(groupName);
            groupPayInfoDO.setGroupNo(groupNo);
            groupPayInfoDO.setGroupShopId(groupShopId);
            groupPayInfoDO.setGroupPayPrice(groupPrice);
            groupPayInfoDO.setStoreId(orderInfoDO.getStoreId());
            groupPayInfoDO.setOrderId(orderInfoDO.getOrderId());
            groupPayInfoDO.setGroupPayType(groupType);
            groupPayInfoMapper.insert(groupPayInfoDO);
            //异步发送微信通知
            workWxService.sendOrderMsg(roomInfoDO.getStoreId(), reqVO.getUserId(), roomInfoDO.getRoomName(), groupPrice, null, reqVO.getPayType(), orderInfoDO.getGroupPayType(), orderNo, orderInfoDO.getStartTime(), orderInfoDO.getEndTime());
        } else {
            //异步发送微信通知
            workWxService.sendOrderMsg(roomInfoDO.getStoreId(), reqVO.getUserId(), roomInfoDO.getRoomName(), totalPrice, couponInfoDO, reqVO.getPayType(), orderInfoDO.getGroupPayType(), orderNo, orderInfoDO.getStartTime(), orderInfoDO.getEndTime());
        }
        return orderInfoDO.getOrderId();

    }

    /**
     * 保存团购券使用记录
     *
     * @param storeId
     * @param orderId
     * @param title
     * @param groupNo
     * @param shopId
     * @param price
     * @param groupType
     */
    private void addGroupPayRecord(Long storeId, Long orderId, String title, String groupNo, String shopId, BigDecimal price, Integer groupType) {
        GroupPayInfoDO groupPayInfoDO = new GroupPayInfoDO();
        groupPayInfoDO.setStoreId(storeId);
        groupPayInfoDO.setOrderId(orderId);
        groupPayInfoDO.setGroupName(title);
        groupPayInfoDO.setGroupShopId(shopId);
        groupPayInfoDO.setGroupNo(groupNo);
        groupPayInfoDO.setGroupPayPrice(price);
        groupPayInfoDO.setGroupPayType(groupType);
        groupPayInfoMapper.insert(groupPayInfoDO);
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
        if (ObjectUtils.isEmpty(reqVO.getUserId())) {
            reqVO.setUserId(getLoginUserId());
        }
        Long userId = reqVO.getUserId();
//        if (reqVO.getMinutes() < 1 || reqVO.getMinutes() % 30 != 0) {
//            throw exception(TIME_UNIT_ERROR);
//        }
        //把订单查出来
        OrderInfoDO orderInfoDO = orderInfoMapper.selectById(reqVO.getOrderId());
        //未开始=0 进行中=1  已完成=2  已取消=3
        switch (orderInfoDO.getStatus()) {
            case 0:
            case 1:
                //未开始和进行中  直接续费
                break;
            case 2:
                //完成 不支持续费
                throw exception(ORDER_STATUS_FINISH_OPRATION_ERROR);
                //已完成，5分钟内可以续费，超过5分钟只能重新下单
//                if (((new Date().getTime() - orderInfoDO.getEndTime().getTime()) / 1000 / 60) > 5) {
//                    throw exception(ORDER_STATUS_FINISH_OPRATION_ERROR);
//                }
            case 3://已经取消，不能续费
                throw exception(ORDER_STATUS_CANCEL_OPRATION_ERROR);
        }
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(orderInfoDO.getRoomId());
        //续费之前仍然再检查一遍 并计算出应付总金额
        Date startTime = orderInfoDO.getEndTime();
        Date endTime = reqVO.getEndTime();
        log.info("订单:{},续费开始时间:{}，结束时间：{}", orderInfoDO.getOrderId(), startTime, endTime);
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(orderInfoDO.getRoomId(), startTime, endTime, null, reqVO.getOrderId(), false, false);
        //订单价格
        BigDecimal totalPrice = new BigDecimal(String.valueOf(wxPayOrderRespVO.getPrice() / 100.0));
        switch (reqVO.getPayType()) {
            case 1://微信
                // 从redis查询 存在的情况才处理，防止重复验证充值
                String redisKey = String.format(WX_PAY_ORDER, reqVO.getOrderNo());
                if (redisTemplate.hasKey(redisKey)) {
                    //如果已经验证成功了 就移除这个订单的信息  避免重复处理
                    redisTemplate.delete(redisKey);
                    //有支付单号，再验证支付是否成功
                    PayOrderDO payOrderDO = payOrderService.getByOrderNo(reqVO.getOrderNo());
                    if (ObjectUtils.isEmpty(payOrderDO)) {
                        throw exception(ORDER_WEIXIN_PAY_ERROR);
                    } else if (!payOrderService.checkWxOrder(payOrderDO.getOrderNo(), payOrderDO.getStoreId(), wxPayOrderRespVO.getPrice())) {
                        throw exception(ORDER_WEIXIN_PAY_ERROR);
                    } else if (!payOrderDO.getPayStatus()) {
                        throw exception(ORDER_WEIXIN_PAY_ERROR);
                    }
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
                        addPayRecord(roomInfoDO.getStoreId(), subtract, AppEnum.user_money_bill_type.PAY.getValue(), 2, storeUserDO.getGiftBalance(), null, "订单：" + orderInfoDO.getOrderNo() + ",续费", userId);
                        if (userBalance.compareTo(BigDecimal.ZERO) > 0) {
                            addPayRecord(roomInfoDO.getStoreId(), userBalance, AppEnum.user_money_bill_type.PAY.getValue(), 1, BigDecimal.ZERO, null, "订单：" + orderInfoDO.getOrderNo() + ",续费", userId);
                        }
                    } else {
                        throw exception(MEMBER_BALANCE_MIN_ERROR);
                    }
                }
                break;
            default:
                throw exception(PAY_TYPE_ERROR);
        }
        //支付完了，增加订单的结束时间
        orderInfoDO.setEndTime(endTime);
        //增加订单金额
        orderInfoDO.setPrice(orderInfoDO.getPrice().add(totalPrice));
        //如果状态是已完成，则状态改成进行中 并触发一次开房间门操作，以实现通电
        if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.FINISH.getValue()) == 0) {
            orderInfoDO.setStatus(AppEnum.order_status.START.getValue());
            deviceService.openRoomDoor(roomInfoDO.getRoomId(), 1);
            if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.USED.getValue()) != 0) {
                roomInfoDO.setStatus(AppEnum.room_status.USED.getValue());
                roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), roomInfoDO.getRoomId());
                clearInfoMapper.cancelByRoomId(roomInfoDO.getRoomId());
            }
        }
        orderInfoMapper.updateById(orderInfoDO);
        //异步发送微信通知
        workWxService.sendRenewMsg(roomInfoDO.getStoreId(), userId, roomInfoDO.getRoomName(), totalPrice, reqVO.getPayType(), orderInfoDO.getOrderNo(), orderInfoDO.getEndTime(), false);
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
        //如果没有传订单id 就返回该用户最新创建的一笔订单
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
            RoomInfoDO newRoomInfo = roomInfoMapper.selectById(roomId);
            RoomInfoDO oldRoomInfo = roomInfoMapper.selectById(orderInfoDO.getRoomId());
            if (newRoomInfo.getType() > oldRoomInfo.getType()) {
                throw exception(ORDER_CHANGE_ROOM_ERROR);
            } else {
                //检查是否可用
                preOrder(roomId, orderInfoDO.getStartTime(), orderInfoDO.getEndTime(), null, null, false, false);
                //开始更换
                orderInfoDO.setRoomId(roomId);
                orderInfoMapper.updateById(orderInfoDO);
                //改新房间的状态  如果房间是空闲，则改成已预订
                if (newRoomInfo.getStatus().compareTo(AppEnum.room_status.ENABLE.getValue()) == 0) {
                    roomInfoMapper.updateStatusById(AppEnum.room_status.PENDDING.getValue(), roomId);
                }
                //改旧房间的状态
                Long oldRoomId = oldRoomInfo.getRoomId();
                //如果有未完成的保洁订单 状态就是待保洁
                int countCurrentByRoomId = clearInfoMapper.countCurrentByRoomId(oldRoomId);
                if (countCurrentByRoomId > 0) {
                    roomInfoMapper.updateStatusById(AppEnum.room_status.CLEAR.getValue(), oldRoomId);
                } else if (orderInfoMapper.countByRoomCurrent(oldRoomId, orderId) > 0) {
                    // 如果当前有订单进行 就改成进行中
                    roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), oldRoomId);
                } else if (orderInfoMapper.countByRoomId(oldRoomId, orderId) > 0) {
                    // 如果后面还有预约 就改成已预定
                    roomInfoMapper.updateStatusById(AppEnum.room_status.PENDDING.getValue(), oldRoomId);
                } else {
                    // 否则 改成空闲
                    roomInfoMapper.updateStatusById(AppEnum.room_status.ENABLE.getValue(), oldRoomId);
                }
                //发送消息到企业微信
                workWxService.sendChangeRoomMsg(orderInfoDO.getStoreId(), orderInfoDO.getOrderNo(), orderInfoDO.getStartTime(), orderInfoDO.getEndTime(), oldRoomInfo.getRoomName(), newRoomInfo.getRoomName(), loginUserId);
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
        CouponInfoDO couponInfoDO = null;
        boolean cancelFlag = true;//默认允许取消订单
        //对于用户  只能取消自己的订单
        if (orderInfoDO.getUserId().compareTo(loginUserId) != 0) {
            throw exception(OPRATION_ERROR);
        }
        //未开始和进行中的订单  并且订单创建时间在5分钟内  都可以取消  其他则不能
        cancelFlag = orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0 || orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0;
        LocalDateTime currentDateTime = LocalDateTime.now(); // 当前时间
        LocalDateTime fiveMinutesAfter = orderInfoDO.getCreateTime().plusMinutes(6);// 订单开始时间5分钟后的时间 多一分钟 给点缓冲时间
        if (currentDateTime.isAfter(fiveMinutesAfter)) {
            //订单创建时间超过了当前时间5分钟
            cancelFlag = false;
        }
        if (cancelFlag) {
            //判断支付方式
            if (!ObjectUtils.isEmpty(orderInfoDO.getGroupPayNo())) {
                GroupPayInfoDO groupPayInfoDO = groupPayInfoMapper.getByOrderId(orderId);
                if (orderInfoDO.getGroupPayType().compareTo(AppEnum.member_group_no_type.MEITUAN.getValue()) == 0) {
                    meituanService.reverseconsume(orderInfoDO.getStoreId(), orderInfoDO.getUserId(), groupPayInfoDO.getGroupNo(), groupPayInfoDO.getGroupShopId());
                } else if (orderInfoDO.getGroupPayType().compareTo(AppEnum.member_group_no_type.DOUYIN.getValue()) == 0) {
                    //verify_id 在前   certificate_id在后
                    String[] split = groupPayInfoDO.getGroupNo().split("-");
                    DouyinCancelReqVO reqVO = new DouyinCancelReqVO();
                    reqVO.setVerify_id(split[0]);
                    reqVO.setCertificate_id(split[1]);
                    douyinService.cancel(reqVO);
                }
                //删除团购券记录
                groupPayInfoMapper.deleteById(groupPayInfoDO.getId());
            } else {
                //实际支付金额为0  就不退款了
                if (orderInfoDO.getPayPrice().compareTo(BigDecimal.ZERO) > 0) {
                    if (orderInfoDO.getPayType().compareTo(AppEnum.order_pay_type.WEIXIN.getValue()) == 0) {
                        //创建微信支付实例
                        WxPayService wxPayService = myWxPayService.init(orderInfoDO.getStoreId());
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
                    couponInfoDO = couponInfoMapper.selectById(orderInfoDO.getCouponId());
                    if (couponInfoDO.getExpriceTime().after(new Date())) {
                        couponInfoDO.setStatus(AppEnum.coupon_status.AVAILABLE.getValue());
                    } else {
                        couponInfoDO.setStatus(AppEnum.coupon_status.EXPIRE.getValue());
                    }
                    couponInfoMapper.updateById(couponInfoDO);
                }
                orderInfoDO.setRefundPrice(orderInfoDO.getPayPrice());
            }
            //被取消的订单已开始了  那就触发一下关门
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                deviceService.closeRoomDoor(orderInfoDO.getRoomId(), 4);
            }
            //设置订单状态为取消
            orderInfoDO.setStatus(AppEnum.order_status.CANCEL.getValue());
            //取消后  如果有未完成的保洁订单 状态就是待保洁
            int countCurrentByRoomId = clearInfoMapper.countCurrentByRoomId(orderInfoDO.getRoomId());
            if (countCurrentByRoomId > 0) {
                roomInfoMapper.updateStatusById(AppEnum.room_status.CLEAR.getValue(), orderInfoDO.getRoomId());
            } else if (orderInfoMapper.countByRoomCurrent(orderInfoDO.getRoomId(), orderId) > 0) {
                // 如果当前有订单进行 就改成进行中
                roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), orderInfoDO.getRoomId());
            } else if (orderInfoMapper.countByRoomId(orderInfoDO.getRoomId(), orderId) > 0) {
                // 如果后面还有预约 就改成已预定
                roomInfoMapper.updateStatusById(AppEnum.room_status.PENDDING.getValue(), orderInfoDO.getRoomId());
            } else {
                // 否则 改成空闲
                roomInfoMapper.updateStatusById(AppEnum.room_status.ENABLE.getValue(), orderInfoDO.getRoomId());
            }
            orderInfoMapper.updateById(orderInfoDO);
            //异步发送微信通知
            workWxService.sendOrderCancelMsg(orderInfoDO.getStoreId(), loginUserId, orderInfoDO.getRoomId(), orderInfoDO.getPayPrice(), couponInfoDO, orderInfoDO.getPayType(), orderInfoDO.getGroupPayType(), orderInfoDO.getOrderNo(), false);
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
                //早于开始时间 判断一下是否能提前开始  最早不能提前6小时开始
                long l1 = (orderInfoDO.getStartTime().getTime() - now.getTime()) / 1000 / 60;
                if (l1 > 360) {
                    throw exception(ORDER_START_TIQIAN_ERROR);
                }
                //对于通宵场，开始时间只能在23时以后 4时之前
                if (orderInfoDO.getNightLong()) {
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
                preOrder(orderInfoDO.getRoomId(), now, orderInfoDO.getEndTime(), null, orderId, false, false);
            }
            //开始订单
            orderInfoDO.setStatus(AppEnum.order_status.START.getValue());
            orderInfoMapper.updateById(orderInfoDO);
            //todo.. 如果房间状态是待保洁  则赠送一张1小时优惠券给会员
            //房间改为进行中
            roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), orderInfoDO.getRoomId());
            //将该房间历史的保洁订单，未开始的  改成取消
//            clearInfoMapper.cancelByRoomId(orderInfoDO.getRoomId());
            //开门开电
//            deviceService.openRoomDoor(orderInfoDO.getRoomId(), null, 4);
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
        boolean night = now.getHours() < 8 && now.getMinutes() == 0;
        log.info("night:{}", night);
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
//                log.info("进行中订单：{}，结束时间:{}", x.getOrderNo(), DateUtils.dateToStr(x.getEndTime(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
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
                    deviceService.closeRoomDoor(x.getRoomId(), 4);
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
                    //如果当前是 0-7点  整点 提醒夜间控制噪音  每笔订单只在第一个整点进行提醒
                    if (night) {
                        //开始时间是0点以后的  从1点开始提醒
                        if (x.getStartTime().getHours() + 1 == now.getHours()) {
                            deviceService.runSound(x.getRoomId(), 5);
                        } else if (now.getHours() == 0) {
                            //开始时间是其他 0时提醒 前日23时开始的订单
                            deviceService.runSound(x.getRoomId(), 5);
                        }
                    }
                }
            });
            if (!org.springframework.util.CollectionUtils.isEmpty(roomIds)) {
                orderInfoMapper.updateStatusByIds(AppEnum.order_status.FINISH.getValue(), orderIds.stream().collect(Collectors.joining(",")));
                roomInfoMapper.updateStatusByIds(AppEnum.room_status.CLEAR.getValue(), roomIds.stream().collect(Collectors.joining(",")));
                //取消掉存在的保洁订单
                clearInfoMapper.cancelByRoomIds(roomIds.stream().collect(Collectors.joining(",")));
                //然后再新增保洁订单
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
//                log.info("未开始订单：{}，开始时间:{}", x.getOrderNo(), DateUtils.dateToStr(x.getStartTime(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
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
//        return payOrderService.checkWxOrder(orderNo, null);
        return false;
    }


    @Override
    public List<AppDiscountRulesRespVO> getDiscountRules(Long storeId) {
        return discountRulesMapper.getDiscountRulesByStoreId(storeId);
    }

    @Override
    @Synchronized
    @Transactional
    public void executeMeituanRefreshTokenJob() {
        log.info("==========     开始执行美团授权定时刷新任务     ==========");
        LocalDateTime now = LocalDateTime.now();
        now = now.plusDays(1);//加一天  用来判断过期
        List<StoreMeituanInfoDO> list = storeMeituanInfoMapper.selectList();
        for (StoreMeituanInfoDO infoDO : list) {
            if (infoDO.getExpiresIn().isBefore(now)) {
                //需要刷新
                meituanService.refreshToken(infoDO.getStoreId(), infoDO.getRefreshToken());
                if (infoDO.getRemainRefreshCount() == 1) {
                    //提醒授权更新
                    workWxService.sendMeiTuanScopeMsg(infoDO.getStoreId());
                }
            }
        }
        //处理ewelink
        log.info("==========     开始执行易微联授权定时刷新任务     ==========");
        ewlService.refushTokenCheck();
        log.info("==========    美团/易微联授权定时刷新任务结束     ==========");

    }

    @Override
    @Transactional
    public void openRoomDoor(Long orderId) {
        OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            //只能操作自己的订单
            if (orderInfoDO.getUserId().compareTo(getLoginUserId()) != 0) {
                throw exception(OPRATION_ERROR);
            }
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
                startOrder(orderId);
                deviceService.openRoomDoor(orderInfoDO.getRoomId(), 1);
            } else if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                deviceService.openRoomDoor(orderInfoDO.getRoomId(), 1);
            } else {
                throw exception(CLEAR_OPEN_DOOR_ERROR);
            }
        } else {
            throw exception(ORDER_NOT_FOUND_ERROR);
        }
    }

    @Override
    @Transactional
    public void openStoreDoor(Long orderId) {
        OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            //只能操作自己的订单
            if (orderInfoDO.getUserId().compareTo(getLoginUserId()) != 0) {
                throw exception(OPRATION_ERROR);
            }
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0
                    || orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                //只能提前6小时开门
                Date now = new Date();
                long l1 = (orderInfoDO.getStartTime().getTime() - now.getTime()) / 1000 / 60;
                if (l1 > 360) {
                    throw exception(ORDER_START_TIQIAN_ERROR);
                }
                deviceService.openStoreDoor(orderInfoDO.getStoreId(), 1);
            } else {
                throw exception(CLEAR_OPEN_DOOR_ERROR);
            }
        } else {
            throw exception(ORDER_NOT_FOUND_ERROR);
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
