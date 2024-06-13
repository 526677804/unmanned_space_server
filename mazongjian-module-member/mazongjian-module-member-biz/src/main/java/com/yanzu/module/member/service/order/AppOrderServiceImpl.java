package com.yanzu.module.member.service.order;

import cn.hutool.core.util.HexUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.common.util.collection.CollectionUtils;
import com.yanzu.framework.common.util.date.DateUtils;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.security.core.LoginUser;
import com.yanzu.framework.security.core.util.SecurityFrameworkUtils;
import com.yanzu.framework.tenant.core.context.TenantContextHolder;
import com.yanzu.module.member.controller.app.order.vo.*;
import com.yanzu.module.member.controller.app.store.vo.AppRoomListVO;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.module.member.dal.dataobject.pkginfo.PkgInfoDO;
import com.yanzu.module.member.dal.dataobject.pkguserinfo.PkgUserInfoDO;
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
import com.yanzu.module.member.dal.mysql.pkginfo.PkgInfoMapper;
import com.yanzu.module.member.dal.mysql.pkguserinfo.PkgUserInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.dal.mysql.storemeituaninfo.StoreMeituanInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.AppUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.enums.AppWxPayTypeEnum;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.douyin.DouyinService;
import com.yanzu.module.member.service.douyin.vo.DouyinCancelReqVO;
import com.yanzu.module.member.service.douyin.vo.DouyinPrepareRespVO;
import com.yanzu.module.member.service.iot.IotService;
import com.yanzu.module.member.service.meituan.MeituanService;
import com.yanzu.module.member.service.meituan.vo.MeituanPrepareRespVO;
import com.yanzu.module.member.service.payorder.PayOrderService;
import com.yanzu.module.member.service.wx.MyWxService;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private PkgInfoMapper pkgInfoMapper;

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
    private MyWxService myWxService;

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
    private PkgUserInfoMapper pkgUserInfoMapper;

    @Resource
    private WorkWxService workWxService;

    @Resource
    private IotService iotService;


    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private StoreMeituanInfoMapper storeMeituanInfoMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;

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
    public WxPayOrderRespVO preOrder(Long userId, Long roomId, Date startTime, Date endTime, CouponInfoDO couponInfoDO, PkgInfoDO pkgInfoDO, Long ignoreOrderId, boolean nightLong, boolean wxpay) {
        //秒位处理为0
        startTime.setSeconds(0);
        endTime.setSeconds(0);
        Date now = new Date();
        Date oldStartTime = new Date(startTime.getTime());
        Date oldEndTime = new Date(endTime.getTime());
        //参数校验
        //开始时间不能小于结束时间
        if (startTime.after(endTime)) {
            throw exception(ORDER_START_TIME_GT_END_ERROR);
        }
        if (ObjectUtils.isEmpty(ignoreOrderId) && startTime.before(now)) {
            //新下单的 开始时间在当前之前，不能超过5分钟  不然间隔太久了
            long l = (now.getTime() - startTime.getTime()) / 1000 / 60;
            if (l >= 5) {
                throw exception(ORDER_START_TIME_LT_NOW_ERROR);
            }
        }
        //查询房间的信息 以及门店的信息
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        //查询出门店的配置信息
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(roomInfoDO.getStoreId());
        if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.DISABLE.getValue()) == 0) {
            throw exception(CLEAR_AND_FINISH_ROOM_STATUS_ERROR);
        }
        //订单时长 （分钟）
        long orderMinutes = Math.abs(ChronoUnit.MINUTES.between(startTime.toInstant(), endTime.toInstant()));
        // 支付类型
        AppWxPayTypeEnum appWxPayTypeEnum = null;
        //检查优惠券是否允许使用
        checkCouponUse(couponInfoDO, nightLong, roomInfoDO.getType(), roomInfoDO.getStoreId(), startTime, endTime);
        //检查套餐是否允许使用
        checkPkgUse(pkgInfoDO, nightLong, roomInfoDO.getType(), roomInfoDO.getStoreId(), startTime, endTime, orderMinutes);
        //计算订单价格
        BigDecimal mathPrice = mathPrice(roomInfoDO.getPrice(), roomInfoDO.getDeposit(), roomInfoDO.getWorkPrice(), storeInfoDO.getWorkPrice(),
                roomInfoDO.getTongxiaoPrice(), storeInfoDO.getTxHour(), startTime, endTime, nightLong, couponInfoDO, pkgInfoDO);
        if (ObjectUtils.isEmpty(ignoreOrderId)) {
            //下单
            appWxPayTypeEnum = AppWxPayTypeEnum.ORDER;
            //如果使用了加时券，则直接增加指定的小时
            if (!ObjectUtils.isEmpty(couponInfoDO) && couponInfoDO.getType().compareTo(AppEnum.coupon_type.JIASHI.getValue()) == 0) {
                endTime = new Date(endTime.getTime() + 1000 * 60 * 60 * couponInfoDO.getPrice().intValue());
            }
            //检查订单时间 是否符合最小下单时间要求
            if ((orderMinutes) < roomInfoDO.getMinHour() * 60) {
                throw exception(ORDER_MIN_HOUR_ERROR);
            }
            //下单需要,检查时间有没有超过提前设置的范围
            Instant instant1 = startTime.toInstant();
            Instant instant2 = now.toInstant();
            ZonedDateTime zonedDateTime1 = instant1.atZone(ZoneId.systemDefault());
            ZonedDateTime zonedDateTime2 = instant2.atZone(ZoneId.systemDefault());
            Duration duration = Duration.between(zonedDateTime1, zonedDateTime2);
            long days = duration.toDays();
            if (days > roomInfoDO.getLeadDay()) {
                throw exception(ORDER_START_TIME_MAX_ERROR);
            }
            //判断清洁时是否允许下单
            if (!storeInfoDO.getClearOpen()) {
                if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.CLEAR.getValue()) == 0) {
                    throw exception(ROOM_CLEAR_SUBMIT_ORDER_ERROR);
                }
            }
            //查询出该房间，存在时间交集的订单 并考虑计算清洁时间
            Integer c = orderInfoMapper.countByPreOrder(roomId, storeInfoDO.getClearTime(), startTime, endTime, ignoreOrderId);
            if (c > 0) {
                throw exception(ORDER_TIME_CHECK_ERROR);
            }
        } else {
            //续费 或者提前开始
            appWxPayTypeEnum = AppWxPayTypeEnum.RENEW;
            //查询出该房间，存在时间交集的订单 不考虑计算清洁时间
            Integer c = orderInfoMapper.countByPreOrder(roomId, 0, startTime, endTime, ignoreOrderId);
            if (c > 0) {
                throw exception(ORDER_TIME_CHECK_ERROR);
            }
        }
        //再检查该房间有没有正被锁定的订单
        String redisKey = "wx_order_lock_room_" + roomId;
        Object rValue = redisTemplate.opsForValue().get(redisKey);
        if (!ObjectUtils.isEmpty(rValue) && !ObjectUtils.isEmpty(userId)) {
            OrderPreReqVO orderPreReqVO = (OrderPreReqVO) rValue;
            //有 再看看锁定的是不是自己
            if (userId.compareTo(orderPreReqVO.getUserId()) != 0) {
                //不是自己  则报错
                throw exception(ORDER_ROOM_SUMBIT_ERROR);
            }
            //是自己就忽略，反正1分钟就解除锁定了
        }
        //再检查是否在禁用时间范围内
        if (!ObjectUtils.isEmpty(roomInfoDO.getBanTimeStart()) && !ObjectUtils.isEmpty(roomInfoDO.getBanTimeStart())) {
            // 禁用时间段列表，包含禁用开始时间和结束时间 new TimeRange("02:00", "08:00")
            LocalTime bHStart = LocalTime.parse(roomInfoDO.getBanTimeStart());
            LocalTime bHEnd = LocalTime.parse(roomInfoDO.getBanTimeEnd());
            //取下单开始时间
            LocalDateTime bTStart = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            bTStart = bTStart.with(bHStart);
            LocalDateTime bTEnd = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            bTEnd = bTEnd.with(bHEnd);
            // 判断是否跨日
            if (bHEnd.isBefore(bHStart)) {
                //跨日了
                bTEnd = bTEnd.plusDays(1);
            }
            LocalDateTime orderStartTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime orderEndTime = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if ((orderStartTime.isBefore(bTStart) && orderEndTime.isBefore(bTStart)) || (orderStartTime.isAfter(bTEnd) && orderEndTime.isAfter(bTEnd))) {
                //时间合法
            } else {
                throw exception(ORDER_TIME_CHECK_ERROR);
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
                WxPayService wxPayService = myWxService.initWxPay(roomInfoDO.getStoreId());
                //获取分账配置
                StoreWxpayConfigDO wxPayConfig = myWxService.getWxPayConfig(roomInfoDO.getStoreId());
                //生成微信支付的订单
                WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
                wxPayUnifiedOrderRequest.setBody("微信支付订单");
                wxPayUnifiedOrderRequest.setOutTradeNo(orderNo);
                wxPayUnifiedOrderRequest.setTotalFee(price);
                wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
                wxPayUnifiedOrderRequest.setNotifyUrl(returnUrl);
                wxPayUnifiedOrderRequest.setTradeType("JSAPI");
                wxPayUnifiedOrderRequest.setProfitSharing(wxPayConfig.getServiceModel() && wxPayConfig.getSplit() ? "Y" : "N");
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
                Long tenantId = TenantContextHolder.getTenantId();
                // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
                if (tenantId == null) {
                    LoginUser user = SecurityFrameworkUtils.getLoginUser();
                    tenantId = user.getTenantId();
                }
                //把这个信息存储到redis，在回调处验证后删除 最长1天过期
                WxPayOrderInfo wxPayOrderInfo = new WxPayOrderInfo(appWxPayTypeEnum, orderNo, getLoginUserId(), tenantId, roomInfoDO.getStoreId()
                        , roomId, oldStartTime, oldEndTime, ObjectUtils.isEmpty(couponInfoDO) ? null : couponInfoDO.getCouponId()
                        , ObjectUtils.isEmpty(pkgInfoDO) ? null : pkgInfoDO.getPkgId(), ignoreOrderId, price, nightLong);
                redisTemplate.opsForValue().set(String.format(WX_PAY_ORDER, orderNo), wxPayOrderInfo, 1, TimeUnit.DAYS);
            }
        }
        log.info("预下单:{}", respVO);
        return respVO;
    }


    /**
     * 检查套餐是否能使用
     *
     * @param pkgInfoDO
     * @param nightLong
     * @param roomType
     * @param storeId
     * @param startTime
     * @param endTime
     */
    private void checkPkgUse(PkgInfoDO pkgInfoDO, boolean nightLong, Integer roomType, Long storeId, Date startTime, Date endTime, long orderMinutes) {
        //有使用 再判断
        if (!ObjectUtils.isEmpty(pkgInfoDO)) {
            //判断购买数量限制
            if (pkgInfoDO.getMaxNum() > 0) {
                int count = pkgUserInfoMapper.countByUserId(pkgInfoDO.getPkgId(), getLoginUserId());
                if (count >= pkgInfoDO.getMaxNum()) {
                    throw exception(PKG_BUY_MAX_NUM_ERROR);
                }
            }
            //判断适用门店
            if (pkgInfoDO.getStoreId().compareTo(storeId) != 0) {
                exception(PKG_USE_STORE_ERROR);
            }
            //有限制的房间类型的  就判断房间类型
            if (!ObjectUtils.isEmpty(pkgInfoDO.getRoomType()) && pkgInfoDO.getRoomType().compareTo(0) != 0) {
                if (pkgInfoDO.getRoomType().compareTo(roomType) != 0) {
                    throw exception(PKG_USE_CHECK_ROOM_TYPE_ERROR);
                }
            }
            //判断时间使用限制
            if (!CollectionUtils.isAnyEmpty(pkgInfoDO.getEnableTime()) || pkgInfoDO.getEnableTime().size() != 24) {
                //取开始时间到结束时间所有的小时
                Set<String> hoursBetween = getHoursBetween(startTime, endTime);
                if (getElementsNotInSet(pkgInfoDO.getEnableTime(), hoursBetween)) {
                    throw exception(PKG_USE_CHECK_TIME_ERROR);
                }
            }
            //判断星期限制
            if (!CollectionUtils.isAnyEmpty(pkgInfoDO.getEnableWeek()) || pkgInfoDO.getEnableWeek().size() != 7) {
                //取开始时间到结束时间所有的week
                Set<String> weeksBetween = getWeeksBetween(startTime, endTime);
                if (getElementsNotInSet(pkgInfoDO.getEnableWeek(), weeksBetween)) {
                    throw exception(PKG_USE_CHECK_WEEK_ERROR);
                }
            }
            //判断时长是否匹配
            if (orderMinutes != pkgInfoDO.getHours() * 60) {
                throw exception(PKG_USE_CHECK_HOUR_ERROR);
            }
            //节假日判断
            //TODO....
        }

    }

    public boolean getElementsNotInSet(List<Integer> list, Set<String> set) {
        Set<String> setCopy = list.stream().map(x -> x.toString()).collect(Collectors.toSet());
        for (String s : set) {
            if (!setCopy.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static Set<String> getHoursBetween(Date startTime, Date endTime) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startTime);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endTime);
        endCal.set(Calendar.MINUTE, 0);
        endCal.set(Calendar.SECOND, 0);
        endCal.set(Calendar.MILLISECOND, 0);

        Set<String> hourSet = new HashSet<>();
        while (startCal.before(endCal) && hourSet.size() < 24) {
            hourSet.add(String.valueOf(startCal.get(Calendar.HOUR_OF_DAY)));
            startCal.add(Calendar.HOUR_OF_DAY, 1);
        }
        return hourSet;
    }

    public static Set<String> getWeeksBetween(Date startTime, Date endTime) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startTime);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endTime);
        endCal.set(Calendar.MINUTE, 0);
        endCal.set(Calendar.SECOND, 0);
        endCal.set(Calendar.MILLISECOND, 0);
        Set<String> weekSet = new HashSet<>();

        while (startCal.before(endCal) && weekSet.size() < 24) {
            //加到结果
            weekSet.add(String.valueOf(startCal.get(Calendar.DAY_OF_WEEK)));
            //增加一天
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return weekSet;
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
            //如果是通宵场，那么优惠券名称必须包含通宵两个字
            if (nightLong) {
                if (!couponInfoDO.getCouponName().contains("通宵")) {
                    throw exception(COUPON_USE_CHECK_ERROR);
                }
            } else {
                //如果不是通宵场，但标题包含了通宵两个字  是不允许的
                if (couponInfoDO.getCouponName().contains("通宵")) {
                    throw exception(COUPON_USE_CHECK_ERROR);
                }
            }
        }
    }


    @Override
    public BigDecimal mathPrice(BigDecimal price, BigDecimal deposit, BigDecimal workPrice, Boolean enableWorkPrice, BigDecimal tongxiaoPrice, Integer txHour,
                                Date startTime, Date endTime, Boolean nightLong, CouponInfoDO couponInfoDO, PkgInfoDO pkgInfoDO) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (!ObjectUtils.isEmpty(pkgInfoDO)) {
            //选了套餐  直接返回套餐的售价
            totalPrice = pkgInfoDO.getPrice();
        } else {
            if (enableWorkPrice) {
                //以订单开始时间算，如果开始时间在周一至周四，那么就按工作日价格计算
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startTime);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.THURSDAY) {
                    //工作日
                    price = workPrice;
                }
            }
            // 计算两个日期的小时差 精确到小数点后两位
            BigDecimal hours = new BigDecimal(String.valueOf((endTime.getTime() - startTime.getTime()) / 1000.0 / 60 / 60)).setScale(2, BigDecimal.ROUND_HALF_UP);
            //计算价格 单价*时长
            //如果是通宵场 要考虑通宵场的价格
            if (nightLong) {
                //如果小于等于设置的通宵场时间   就按通宵场价格
                if (hours.compareTo(new BigDecimal(txHour)) <= 0) {
                    totalPrice = tongxiaoPrice;
                } else {
                    //大于 要用多于的时间*单价 再加上通宵场的价格
                    BigDecimal addPrice = hours.subtract(new BigDecimal(txHour)).multiply(price);
                    totalPrice = tongxiaoPrice.add(addPrice);
                }
            } else {
                //否则就是单价*时长
                totalPrice = price.multiply(hours);
            }
            //判断使用优惠券的情况
            if (!ObjectUtils.isEmpty(couponInfoDO)) {
                //判断类型
                switch (couponInfoDO.getType()) {
                    case 1://1抵扣券
                        //判断门槛
                        if (couponInfoDO.getMinUsePrice().compareTo(hours) > 0) {
                            throw exception(COUPON_MIN_USER_PRICE_ERROR);
                        }
                        //抵扣 并重新算价格
                        if (couponInfoDO.getPrice().compareTo(hours) >= 0) {
                            //直接抵扣完，价格设置为0
                            totalPrice = BigDecimal.ZERO;
                        } else {
                            hours = hours.subtract(couponInfoDO.getPrice());
                            totalPrice = price.multiply(hours);
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
                    case 3: //3加时券
                        //判断门槛
                        if (couponInfoDO.getMinUsePrice().compareTo(hours) > 0) {
                            throw exception(COUPON_MIN_USER_PRICE_ERROR);
                        }
                        break;
                }
            }
        }
        if (deposit.compareTo(BigDecimal.ZERO) != 0) {
            //有押金  要加上押金的钱
            totalPrice = totalPrice.add(deposit);
        }
        //结果保留2位小数
        return totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
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


    /**
     * 团购券合法性检查
     *
     * @param title
     * @param startTime
     * @param endTime
     * @param roomType
     * @param nightLong
     */
    private void checkGroupNo(String title, Date startTime, Date endTime, Integer roomType, boolean nightLong, Integer txStartHour, Integer txHour) {
        if (nightLong) {
            //团购的通宵场 要求团购券必须包含 “通宵”两个字
            if (title.indexOf("通宵") == -1) {
                throw exception(GOURP_NO_PAY_TIME_HOUR_CHECK_ERROR);
            }
            //再判断通宵的开始时间是不是在设置的规则范围内  因为团购的通宵 只能到时间后开始
            if (startTime.getHours() < txStartHour) {
                throw exception(CHECK_TONGXIAO_TIME_ERROR);
            }
        }
        //判断工作日限制情况  标题包含工作日和周一 就视为工作日券
        if (title.indexOf("工作日") != -1 || title.indexOf("周一") != -1 || title.indexOf("周四") != -1 || title.indexOf("闲时") != -1) {
            //仅工作日周一 - 周四可用
            checkWorkDay(startTime);
        }
        //判断包间限制情况  标题包含：不限包间
        if (title.indexOf("不限包间") != -1
                || title.indexOf("包间通用") != -1
                || title.indexOf("任意包间") != -1
                || title.indexOf("不分包间") != -1
                || title.indexOf("所有包间") != -1
                || title.indexOf("全部包间") != -1
                || title.indexOf("包间任选") != -1
                || title.indexOf("不限房间") != -1
                || title.indexOf("任意房间") != -1
                || title.indexOf("不分房间") != -1
                || title.indexOf("所有房间") != -1
                || title.indexOf("全部房间") != -1
                || title.indexOf("房间任选") != -1
                || title.indexOf("不限球桌") != -1
                || title.indexOf("任意球桌") != -1
                || title.indexOf("不分球桌") != -1
                || title.indexOf("所有球桌") != -1
                || title.indexOf("全部球桌") != -1
                || title.indexOf("球桌任选") != -1) {
            //不校验
        } else {
            Integer checkRoomType = 0;
            if (title.indexOf("商务包") != -1) {
                //商务包
                checkRoomType = AppEnum.room_type.SW.getValue();
            } else if (title.indexOf("豪包") != -1) {
                //豪包
                checkRoomType = AppEnum.room_type.HAO.getValue();
            } else if (title.indexOf("大包") != -1) {
                //大包
                checkRoomType = AppEnum.room_type.DA.getValue();
            } else if (title.indexOf("中包") != -1) {
                //中包
                checkRoomType = AppEnum.room_type.ZHONG.getValue();
            } else if (title.indexOf("小包") != -1) {
                //小包
                checkRoomType = AppEnum.room_type.XIAO.getValue();
            } else {
                //一个都没匹配上  那就默认小包  但是通宵场不写 就默认所有
                if (!nightLong) {
                    checkRoomType = AppEnum.room_type.XIAO.getValue();
                }
            }
            if (checkRoomType != 0 && roomType.compareTo(checkRoomType) != 0) {
                throw exception(GOURP_NO_PAY_ROOM_TYPE_CHECK_ERROR);
            }
        }
        int timeHour = 0;
        //判断时长是否匹配 通宵场根据门店的设置来校验
        if (nightLong) {
            timeHour = txHour;
        } else {
            int timeIndex = title.indexOf("个小时");
            if (timeIndex == -1) {
                //没找到 再尝试找一下  “个小时”
                timeIndex = title.indexOf("小时");
            }
            //还是没找到  就报错了
            if (timeIndex == -1) {
                throw exception(CHECK_GROUP_NO_TIME_ERROR);
            }
            // 取时间
            String timeStr = title.substring(timeIndex - 1, timeIndex);
            timeHour = Integer.valueOf(timeStr);
        }
        long l = (endTime.getTime() - startTime.getTime()) / 1000 / 60;
        if (l / 60 != timeHour) {
            throw exception(GOURP_NO_PAY_TIME_HOUR_CHECK_ERROR);
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
        //定义一些参数 备用
        String orderNo = reqVO.getOrderNo();
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
        CouponInfoDO couponInfoDO = null;
        if (!ObjectUtils.isEmpty(reqVO.getCouponId())) {
            couponInfoDO = couponInfoMapper.selectById(reqVO.getCouponId());
        }
        PkgInfoDO pkgInfoDO = null;
        if (!ObjectUtils.isEmpty(reqVO.getPkgId())) {
            pkgInfoDO = pkgInfoMapper.selectById(reqVO.getPkgId());
        }
        //下单之前仍然再检查一遍 并计算出应付总金额
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(reqVO.getUserId(), reqVO.getRoomId(), reqVO.getStartTime(), reqVO.getEndTime(),
                couponInfoDO, pkgInfoDO, null, reqVO.getNightLong(), false);
        BigDecimal totalPrice = new BigDecimal(String.valueOf(wxPayOrderRespVO.getPrice() / 100.0));
        BigDecimal oldPrice = new BigDecimal(String.valueOf(wxPayOrderRespVO.getPrice() / 100.0));
        //判断是否有填团购券  先预声明一些团购要的字段
        String groupName = "";
        String groupShopId = "";
        String groupNo = "";
        Integer groupType = null;
        BigDecimal groupPrice = BigDecimal.ZERO;
        if (!ObjectUtils.isEmpty(reqVO.getGroupPayNo())) {
            StoreInfoDO storeInfoDO = storeInfoMapper.selectById(roomInfoDO.getStoreId());
            //设置订单的支付类型为团购
            reqVO.setPayType(AppEnum.order_pay_type.TUANGOU.getValue());
            //处理掉中间有空格的情况
            reqVO.setGroupPayNo(reqVO.getGroupPayNo().replaceAll(" ", ""));
            //判断是抖音券还是美团券
            if (reqVO.getGroupPayNo().length() <= 13) {
                //美团券
                groupType = AppEnum.member_group_no_type.MEITUAN.getValue();
                //查询券信息
                MeituanPrepareRespVO prepare = meituanService.prepare(roomInfoDO.getStoreId(), reqVO.getGroupPayNo());
                groupName = prepare.getTitle();
                groupNo = reqVO.getGroupPayNo();
                groupPrice = prepare.getPayAmount();
                groupShopId = prepare.getDealId();
                checkGroupNo(prepare.getTitle(), reqVO.getStartTime(), reqVO.getEndTime(), roomInfoDO.getType(), reqVO.getNightLong(), storeInfoDO.getTxStartHour(), storeInfoDO.getTxHour());
                //检验通过  把团购券给使用了
                meituanService.consume(roomInfoDO.getStoreId(), reqVO.getUserId(), reqVO.getGroupPayNo(), groupShopId);
            } else {
                //抖音券
                groupType = AppEnum.member_group_no_type.DOUYIN.getValue();
                DouyinPrepareRespVO prepare = douyinService.prepare(reqVO.getGroupPayNo());
                groupName = prepare.getTitle();
                groupPrice = new BigDecimal(String.valueOf(prepare.getPayAmount() / 100.0));
                checkGroupNo(prepare.getTitle(), reqVO.getStartTime(), reqVO.getEndTime(), roomInfoDO.getType(), reqVO.getNightLong(), storeInfoDO.getTxStartHour(), storeInfoDO.getTxHour());
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
                couponInfoMapper.updateById(new CouponInfoDO().setCouponId(couponInfoDO.getCouponId()).setStatus(AppEnum.coupon_status.USED.getValue()));
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
                        if (!ObjectUtils.isEmpty(reqVO.getPkgId())) {
                            //检查套餐是否支持余额支付
                            if (!pkgInfoDO.getBalanceBuy()) {
                                throw exception(PKG_ORDER_PAY_TYPE_ERROR);
                            }
                        }
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
        orderInfoDO.setOrderKey(HexUtil.encodeHexStr(reqVO.getOrderNo() + UUID.randomUUID().toString()));
        orderInfoDO.setStoreId(roomInfoDO.getStoreId());
        orderInfoDO.setRoomId(roomInfoDO.getRoomId());
        orderInfoDO.setUserId(reqVO.getUserId());
        orderInfoDO.setStartTime(reqVO.getStartTime());
        //处理加时券
        if (!ObjectUtils.isEmpty(couponInfoDO) && couponInfoDO.getType().compareTo(AppEnum.coupon_type.JIASHI.getValue()) == 0) {
            reqVO.setEndTime(new Date(reqVO.getEndTime().getTime() + 1000 * 60 * 60 * couponInfoDO.getPrice().intValue()));
        }
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

        //如果房间状态是待清洁，就发送提醒保洁的通知
        if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.CLEAR.getValue()) == 0) {
            //异步发送微信通知
            workWxService.sendOrderClearMsg(roomInfoDO.getStoreId(), roomInfoDO.getRoomName(), orderInfoDO.getStartTime(), orderInfoDO.getEndTime());
        }
        //如果房间状态是空闲，就改成已预定
        else if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.ENABLE.getValue()) == 0) {
            roomInfoDO.setStatus(AppEnum.room_status.PENDING.getValue());
            roomInfoMapper.updateById(roomInfoDO);
        }
        //如果使用了套餐 增加套餐使用记录
        if (!ObjectUtils.isEmpty(reqVO.getPkgId())) {
            PkgUserInfoDO pkgUserInfoDO = new PkgUserInfoDO();
            pkgUserInfoDO.setPkgId(reqVO.getPkgId());
            pkgUserInfoDO.setUserId(reqVO.getUserId());
            pkgUserInfoDO.setStoreId(orderInfoDO.getStoreId());
            pkgUserInfoDO.setOrderId(orderInfoDO.getOrderId());
            pkgUserInfoDO.setStatus(AppEnum.coupon_status.USED.getValue());
            pkgUserInfoMapper.insert(pkgUserInfoDO);
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
        checkRepeatOrder(roomInfoDO.getStoreId(), roomInfoDO.getRoomId(), roomInfoDO.getRoomName(), reqVO.getStartTime(), reqVO.getEndTime(), reqVO.getUserId());
        return orderInfoDO.getOrderId();

    }

    @Async
    public void checkRepeatOrder(Long storeId, Long roomId, String roomName, Date startTime, Date endTime, Long userId) {
        OrderInfoDO repeatOrder = orderInfoMapper.getRepeatOrder(roomId, startTime);
        if (!ObjectUtils.isEmpty(repeatOrder) && repeatOrder.getUserId().compareTo(userId) == 0) {
            //用户重复下单
            workWxService.sendRepeatOrderMsg(storeId, roomName, startTime, endTime, userId);
        }

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
        int randomNum = random.nextInt(1000000000);
        String randomNumString = String.format("%09d", randomNum);
        return currentDate + randomNumString;
    }

    @Override
    @Transactional
    public void renew(OrderRenewalReqVO reqVO) {
        if (ObjectUtils.isEmpty(reqVO.getUserId())) {
            reqVO.setUserId(getLoginUserId());
        }
        Long userId = reqVO.getUserId();
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
//                throw exception(ORDER_STATUS_FINISH_OPRATION_ERROR);
//                已完成，5分钟内可以续费，超过5分钟只能重新下单
                if (((new Date().getTime() - orderInfoDO.getEndTime().getTime()) / 1000 / 60) > 5) {
                    throw exception(ORDER_STATUS_FINISH_OPRATION_ERROR);
                }
                break;
            case 3://已经取消，不能续费
                throw exception(ORDER_STATUS_CANCEL_OPRATION_ERROR);
        }
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(orderInfoDO.getRoomId());
        //续费之前仍然再检查一遍 并计算出应付总金额
        Date startTime = orderInfoDO.getEndTime();
        Date endTime = reqVO.getEndTime();
        log.info("订单:{},续费开始时间:{}，结束时间：{}", orderInfoDO.getOrderId(), startTime, endTime);
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(userId, orderInfoDO.getRoomId(), startTime, endTime, null, null, reqVO.getOrderId(), false, false);
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
            deviceService.openRoomDoor(userId, roomInfoDO.getStoreId(), roomInfoDO.getRoomId(), 1);
            roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), roomInfoDO.getRoomId());
            clearInfoMapper.cancelByRoomId(roomInfoDO.getRoomId());
        }
        orderInfoMapper.updateById(orderInfoDO);
        //异步发送微信通知
        workWxService.sendRenewMsg(roomInfoDO.getStoreId(), userId, roomInfoDO.getRoomName(), totalPrice, reqVO.getPayType(), orderInfoDO.getOrderNo(), orderInfoDO.getEndTime(), false);
        //todo...如果有已接单的保洁订单 发消息通知保洁时间延后了
    }


    @Override
    public PageResult<OrderListRespVO> getOrderPage(OrderPageReqVO reqVO) {
        IPage<OrderListRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        orderInfoMapper.getOrderPage(page, reqVO);
        if (!org.springframework.util.CollectionUtils.isEmpty(page.getRecords())) {
            //如果状态是已取消以外的状态  并且订单结束时间不超过5分钟，那么允许续费
            LocalDateTime now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            page.getRecords().forEach(x -> {
                x.setRenewBtn(false);
                if (!ObjectUtils.isEmpty(x.getRoomImg())) {
                    x.setRoomImg(x.getRoomImg().split(",")[0]);
                }
                if (x.getStatus().compareTo(AppEnum.order_status.CANCEL.getValue()) != 0) {
                    LocalDateTime endDate = x.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusMinutes(5);
                    x.setRenewBtn(endDate.isAfter(now));
                }
            });
        }
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public OrderInfoAppRespVO getOrderInfo(Long orderId, String orderKey) {
        //如果没有传订单id 就返回该用户最新创建的一笔订单
        OrderInfoAppRespVO orderInfo = null;
        if (StringUtils.isEmpty(orderKey) || "null".equals(orderKey)) {
            //校验权限
            orderInfo = orderInfoMapper.getOrderInfo(orderId, null, getLoginUserId());
        } else {
            //对比key
            orderInfo = orderInfoMapper.getOrderInfo(null, orderKey, null);
        }
        if (ObjectUtils.isEmpty(orderInfo)) {
            throw exception(ORDER_NOT_FOUND_ERROR);
        }
        if (!ObjectUtils.isEmpty(orderInfo)) {
            //如果有密码锁网关，设置一下网关id 用于远程开锁
            if (deviceService.countGateway(orderInfo.getStoreId()) > 0) {
                orderInfo.setGatewayId(1L);
            }
            if (!ObjectUtils.isEmpty(orderInfo.getRoomImg())) {
                orderInfo.setRoomImg(orderInfo.getRoomImg().split(",")[0]);
            }
            if (orderInfo.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0 || orderInfo.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                orderInfo.setRenewBtn(true);
            } else {
                orderInfo.setRenewBtn(false);
                //订单key给设置为空 不允许好友再使用
                orderInfo.setOrderKey("");
            }
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
                preOrder(loginUserId, roomId, orderInfoDO.getStartTime(), orderInfoDO.getEndTime(), null, null, null, false, false);
                //开始更换
                orderInfoDO.setRoomId(roomId);
                orderInfoMapper.updateById(orderInfoDO);
                //改新房间的状态
                flushRoomStatus(roomId);
                //改旧房间的状态
                Long oldRoomId = oldRoomInfo.getRoomId();
                flushRoomStatus(oldRoomId);
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
                        //如果有使用套餐，则把套餐设置过期  然后再退款
                        PkgUserInfoDO pkgUserInfoDO = pkgUserInfoMapper.getByOrderId(orderId);
                        if (!ObjectUtils.isEmpty(pkgUserInfoDO)) {
                            pkgUserInfoMapper.updateById(new PkgUserInfoDO().setId(pkgUserInfoDO.getId()).setStatus(AppEnum.coupon_status.EXPIRE.getValue()));
                        }
                        //创建微信支付实例
                        WxPayService wxPayService = myWxService.initWxPay(orderInfoDO.getStoreId());
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
                deviceService.closeRoomDoor(loginUserId, orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), 1);
            }
            //设置订单状态为取消
            orderInfoDO.setStatus(AppEnum.order_status.CANCEL.getValue());
            orderInfoMapper.updateById(orderInfoDO);
            flushRoomStatus(orderInfoDO.getRoomId());
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
//        if (orderInfoDO.getUserId().compareTo(loginUserId) != 0) {
//            throw exception(OPRATION_ERROR);
//        }
        Date now = new Date();
        now.setSeconds(0);//秒数取0 方便计算
        //只有未开始的订单才能开始
        if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
            //判断当前的时间是否在订单开始时间之前
            if (now.before(orderInfoDO.getStartTime())) {
                //对于通宵场 不能提前开始
                if (orderInfoDO.getNightLong()) {
                    throw exception(TONGXIAO_ORDER_START_ERROR);
                } else {
                    //早于开始时间 判断一下是否能提前开始  最早不能提前房间设置的时间
                    RoomInfoDO roomInfoDO = roomInfoMapper.selectById(orderInfoDO.getRoomId());
                    long l1 = (orderInfoDO.getStartTime().getTime() - now.getTime()) / 1000 / 60 / 60;
                    if (l1 > roomInfoDO.getLeadHour()) {
                        throw exception(ORDER_START_TIQIAN_ERROR);
                    }
                    //新的结束时间 等于当前时间加上订单的时长
                    long l = now.getTime() + (orderInfoDO.getEndTime().getTime() - orderInfoDO.getStartTime().getTime());
                    Date endTime = new Date(l);
                    endTime.setSeconds(0);
                    //更改订单的开始和完成时间
                    orderInfoDO.setEndTime(endTime);
                    log.info("订单：{}，提前开始消费！", orderInfoDO.getOrderNo());
                    orderInfoDO.setStartTime(now);
                    //校验时间冲突
                    preOrder(loginUserId, orderInfoDO.getRoomId(), now, orderInfoDO.getEndTime(), null, null, orderId, false, false);
                }
            }
            //开始订单
            orderInfoDO.setStatus(AppEnum.order_status.START.getValue());
            orderInfoMapper.updateById(orderInfoDO);
            //房间改为进行中
            roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), orderInfoDO.getRoomId());
        } else {
            throw exception(ORDER_START_OPRATION_ERROR);
        }

    }

    /**
     * 订单处理的定时任务，每分钟执行一次， 用于到时间开始订单 或者 结束订单
     */
    @Override
    @Synchronized
    public void executeOrderJob() {
        log.info("==========     开始执行订单定时检查任务     ==========");
        Date now = new Date();
        now.setSeconds(0);
        log.info("当前时间:{}", DateUtils.dateToStr(now, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
        boolean night = now.getHours() < 8 && now.getMinutes() == 0;//是否深夜
        log.info("night:{}", night);
        //取出所有需要处理的订单
        List<OrderInfoDO> orderList = orderInfoMapper.getListByJob();
        if (!CollectionUtils.isAnyEmpty(orderList)) {
            Set<Long> startRoomIds = new HashSet<>();
            Set<Long> endRoomIds = new HashSet<>();
            Set<Long> startOrderIds = new HashSet<>();
            Set<Long> endOrderIds = new HashSet<>();
            List<ClearInfoDO> clearInfoDOList = new ArrayList<>();
            //按照门店分组，因为不同的门店，有不同的规则
            Map<Long, List<OrderInfoDO>> listByStoreId = orderList.stream().collect(Collectors.groupingBy(x -> x.getStoreId()));
            listByStoreId.entrySet().forEach(v -> {
                //先查询出门店信息 以读取配置
                StoreInfoDO storeInfoDO = storeInfoMapper.selectById(v.getKey());
                v.getValue().forEach(x -> {
                    if (x.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
                        //未开始 达到预订时间后，就开始订单
                        if (x.getStartTime().before(now)) {
                            startRoomIds.add(x.getRoomId());
                            startOrderIds.add(x.getOrderId());
                        }
                    } else if (x.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                        //进行中 主要是完成订单，和关电
                        try {
                            if (x.getEndTime().before(now)) {
                                //关电
                                deviceService.closeRoomDoor(null, x.getStoreId(), x.getRoomId(), 4);
                                //如果该门店，没有设置延时关灯，那么还需要关灯
                                if (!storeInfoDO.getDelayLight()) {
                                    deviceService.closeLightByRoomId(null, x.getStoreId(), x.getRoomId(), 4);
                                }
                                endRoomIds.add(x.getRoomId());
                                endOrderIds.add(x.getOrderId());
                                //添加保洁记录
                                ClearInfoDO clearInfoDO = new ClearInfoDO();
                                clearInfoDO.setOrderId(x.getOrderId()).setStoreId(x.getStoreId()).setOrderNo(x.getOrderNo()).setRoomId(x.getRoomId());
                                clearInfoDOList.add(clearInfoDO);
                            } else {
                                //检查距离结束的时间，发送语音提醒
                                long minutes = Math.abs(ChronoUnit.MINUTES.between(now.toInstant(), x.getEndTime().toInstant()));
                                long minutesStart = Math.abs(ChronoUnit.MINUTES.between(now.toInstant(), x.getStartTime().toInstant()));
                                if (minutesStart == 3) {
                                    //开始3分钟时 播放欢迎语
                                    deviceService.runSound(x.getRoomId(), 1);
                                } else if (minutes == 30) {
                                    deviceService.runSound(x.getRoomId(), 2);
                                }
                                //暂时取消15分钟时的提醒
                                //                                else if (minutes == 15) {
                                //                                    deviceService.runSound(x.getRoomId(), 3);
                                //                                }
                                else if (minutes == 5) {
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
                        } catch (Exception e) {
                            //异常时不影响其他订单关闭
                            log.error(e.getMessage());
//                                e.printStackTrace();
//                                throw new RuntimeException(e);
                        }
                    } else if (x.getStatus().compareTo(AppEnum.order_status.FINISH.getValue()) == 0) {
                        //已完成  主要是处理延时关电的
                        //如果店铺不需要延时关电，就不处理了
                        try {
                            if (storeInfoDO.getDelayLight()) {
                                long minutes = Math.abs(ChronoUnit.MINUTES.between(now.toInstant(), x.getEndTime().toInstant()));
                                //本来是5分钟 这里提前一分钟 避免与设置的订单结束后5分钟才能预订起冲突
                                if (minutes == 4) {
                                    deviceService.closeLightByRoomId(null, x.getStoreId(), x.getRoomId(), 4);
                                }
                            }
                        } catch (Exception e) {
                            //异常时不影响其他订单关闭
                            log.error(e.getMessage());
//                                throw new RuntimeException(e);
                        }
                    }
                });
            });
            //开始处理
            // 手动提交事务
            TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
            try {
                if (!CollectionUtils.isAnyEmpty(startRoomIds)) {
                    //批量修改房间状态为进行中
                    roomInfoMapper.updateStatusByIds(AppEnum.room_status.USED.getValue(), startRoomIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                    //批量修改订单状态为进行中
                    orderInfoMapper.updateStatusByIds(AppEnum.order_status.START.getValue(), startOrderIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                }
                if (!CollectionUtils.isAnyEmpty(endRoomIds)) {
                    //批量修改房间状态为待清洁
                    roomInfoMapper.updateStatusByIds(AppEnum.room_status.CLEAR.getValue(), endRoomIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                    //批量修改订单状态为已完成
                    orderInfoMapper.updateStatusByIds(AppEnum.order_status.FINISH.getValue(), endOrderIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                    //取消掉这些房间存在的历史保洁订单
                    clearInfoMapper.cancelByRoomIds(endRoomIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                    //然后再新增本次的保洁订单
                    clearInfoMapper.insertBatch(clearInfoDOList);
                    //发送需要保洁的微信通知
                    sendClearMsg(endRoomIds);
                }
                transactionManager.commit(transaction);
            } catch (Exception e) {
                // 发生异常时回滚事务
                transactionManager.rollback(transaction);
//                throw new RuntimeException(e);
            }
        }
        log.info("==========     订单定时检查任务执行完成     ==========");

    }


    @Async
    protected void sendClearMsg(Set<Long> roomIds) {
        String dateStr = DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
        //查询出所有房间
        List<AppRoomListVO> roomList = roomInfoMapper.getListByIds(roomIds);
        //开始发消息
        for (AppRoomListVO vo : roomList) {
            StringBuffer sb = new StringBuffer();
            sb.append("订单已结束,待清洁通知\n");
            sb.append(">门店名称:").append(vo.getStoreName()).append("\n");
            sb.append(">房间名称:").append(vo.getRoomName()).append("\n");
            sb.append(">时间:").append(dateStr).append("\n");
            workWxService.sendClearMsg(vo.getOrderWebhook(), sb.toString());
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
//    @Synchronized
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
        log.info("==========    美团/硬件平台授权定时刷新任务结束     ==========");

    }

    @Override
    @Transactional
    public void openRoomDoor(String orderKey) {
        OrderInfoDO orderInfoDO = orderInfoMapper.selectOne(new LambdaQueryWrapperX<OrderInfoDO>().eq(OrderInfoDO::getOrderKey, orderKey));
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
                //未开始的订单则直接开始
                startOrder(orderInfoDO.getOrderId());
                //然后触发开电
                deviceService.openRoomDoor(orderInfoDO.getUserId(), orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), 1);
            } else if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                deviceService.openRoomDoor(orderInfoDO.getUserId(), orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), 1);
            } else {
                throw exception(CLEAR_OPEN_DOOR_ERROR);
            }
        } else {
            throw exception(ORDER_NOT_FOUND_ERROR);
        }
    }

    @Override
    @Transactional
    public void openStoreDoor(String orderKey) {
        OrderInfoDO orderInfoDO = orderInfoMapper.selectOne(new LambdaQueryWrapperX<OrderInfoDO>().eq(OrderInfoDO::getOrderKey, orderKey));
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0 || orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                //只能提前X小时开门
                RoomInfoDO roomInfoDO = roomInfoMapper.selectById(orderInfoDO.getRoomId());
                Date now = new Date();
                long l1 = (orderInfoDO.getStartTime().getTime() - now.getTime()) / 1000 / 60 / 60;
                if (l1 > roomInfoDO.getLeadHour()) {
                    throw exception(ORDER_START_TIQIAN_ERROR);
                }
                deviceService.openStoreDoor(orderInfoDO.getUserId(), orderInfoDO.getStoreId(), 1);
            } else {
                throw exception(CLEAR_OPEN_DOOR_ERROR);
            }
        } else {
            throw exception(ORDER_NOT_FOUND_ERROR);
        }

    }

    @Override
    @Transactional
    public void openRoomLock(String orderKey) {
        OrderInfoDO orderInfoDO = orderInfoMapper.selectOne(new LambdaQueryWrapperX<OrderInfoDO>().eq(OrderInfoDO::getOrderKey, orderKey));
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
                //未开始的订单则直接开始
                startOrder(orderInfoDO.getOrderId());
                //然后触发开门开电
                deviceService.openRoomBlueLock(orderInfoDO.getUserId(), orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), 1);
                deviceService.openRoomDoor(orderInfoDO.getUserId(), orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), 1);
            } else if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                deviceService.openRoomBlueLock(orderInfoDO.getUserId(), orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), 1);
            } else {
                throw exception(CLEAR_OPEN_DOOR_ERROR);
            }
        } else {
            throw exception(ORDER_NOT_FOUND_ERROR);
        }

    }

    @Override
    public int countByUserAndStoreId(Long userId, Long storeId) {
        return orderInfoMapper.countByUserAndStoreId(userId, storeId);
    }

    @Override
    public void lockWxOrder(OrderPreReqVO reqVO) {
        //以房间id作为key
        String redisKey = "wx_order_lock_room_" + reqVO.getRoomId();
        Long userId = getLoginUserId();
        if (redisTemplate.hasKey(redisKey)) {
            //已经有了，取出来看看是不是用一个用户
            OrderPreReqVO value = (OrderPreReqVO) redisTemplate.opsForValue().get(redisKey);
            if (value.getUserId().compareTo(userId) != 0) {
                //不同 说明是冲突的，不锁定订单
                throw exception(ORDER_ROOM_SUMBIT_ERROR);
            }
            //还是这个用户  就删除之前的  把最新的订单信息锁定
        }
        //订单信息作为value,1分钟有效
        reqVO.setUserId(userId);
        redisTemplate.opsForValue().set(redisKey, reqVO, 1, TimeUnit.MINUTES);
    }

    @Override
    public void flushRoomStatus(Long roomId) {
        if (orderInfoMapper.countByRoomCurrent(roomId, null) > 0) {
            // 如果房间当前有订单进行 就改成进行中
            roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), roomId);
        } else if (clearInfoMapper.countCurrentByRoomId(roomId) > 0) {
            //如果有未完成的保洁订单 状态就是待保洁
            roomInfoMapper.updateStatusById(AppEnum.room_status.CLEAR.getValue(), roomId);
        } else if (orderInfoMapper.countByRoomId(roomId, null) > 0) {
            // 如果后面还有预约 就改成已预定
            roomInfoMapper.updateStatusById(AppEnum.room_status.PENDING.getValue(), roomId);
        } else {
            // 否则 改成空闲
            roomInfoMapper.updateStatusById(AppEnum.room_status.ENABLE.getValue(), roomId);
        }
    }

    @Override
    public int countNewUserByStoreId(Long userId, Long storeId) {
        return orderInfoMapper.countNewUserByStoreId(userId, storeId);
    }

    @Override
    public OrderInfoAppRespVO getOrderByRoomId(Long roomId) {
        OrderInfoDO orderInfoDO = orderInfoMapper.getByRoomCurrent(roomId);
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            OrderInfoAppRespVO orderInfo = getOrderInfo(orderInfoDO.getOrderId(), null);
            if (!ObjectUtils.isEmpty(orderInfo)) {
                //因为安全问题，不返回order key
                orderInfo.setOrderKey(null);
            }
            return orderInfo;
        } else {
            throw exception(ORDER_NOT_FOUND_ERROR);
        }
    }

}
