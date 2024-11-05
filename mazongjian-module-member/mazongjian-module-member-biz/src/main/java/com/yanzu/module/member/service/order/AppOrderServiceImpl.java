package com.yanzu.module.member.service.order;

import cn.hutool.core.util.HexUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
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
import com.yanzu.module.member.service.groupPay.GroupPayInfoService;
import com.yanzu.module.member.service.iot.IotDeviceService;
import com.yanzu.module.member.service.iot.IotGroupPayService;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareRespVO;
import com.yanzu.module.member.service.meituan.MeituanService;
import com.yanzu.module.member.service.payorder.PayOrderService;
import com.yanzu.module.member.service.wx.MyWxService;
import com.yanzu.module.member.service.wx.WorkWxService;
import com.yanzu.module.system.api.social.SocialUserApi;
import com.yanzu.module.system.enums.social.SocialTypeEnum;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
    private IotDeviceService iotDeviceService;

    @Resource
    private IotGroupPayService iotGroupPayService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Resource
    private GroupPayInfoService groupPayInfoService;

    @Resource
    private StoreMeituanInfoMapper storeMeituanInfoMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Value("${wx.pay.returnUrl}")
    private String returnUrl;


    /**
     * @param roomId        房间id
     * @param payType       支付方式
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param couponInfoDO  优惠券
     * @param ignoreOrderId 忽略校验的订单id，用于更换房间 或者提前开始订单
     * @param nightLong     是否通宵
     * @param wxpay         是否微信下单
     * @return
     */
    @Override
    public WxPayOrderRespVO preOrder(Long userId, Integer payType, Long roomId, Date startTime, Date endTime, CouponInfoDO couponInfoDO, PkgInfoDO pkgInfoDO, Long ignoreOrderId, boolean nightLong, boolean wxpay) {
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
        if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.DISABLE.getValue()) == 0) {
            throw exception(CLEAR_AND_FINISH_ROOM_STATUS_ERROR);
        }
        //查询出门店的配置信息
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(roomInfoDO.getStoreId());
        if (storeInfoDO.getStatus().compareTo(AppEnum.store_status.ENABLE.getValue()) != 0) {
            throw exception(STORE_STORE_IS_DISABLE);
        }
        //如果是通宵场  开始时间必须大于设置的通宵起始时间
        if (nightLong) {
            if (startTime.getHours() < storeInfoDO.getTxStartHour() && startTime.getHours() > 4) {
                throw exception(CHECK_TONGXIAO_TIME_ERROR);
            }
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
            long diffHour = (startTime.getTime() - now.getTime()) / 1000 / 60 / 60;
            if (diffHour > roomInfoDO.getLeadDay() * 24) {
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
        int totalPrice = mathPrice.multiply(BigDecimal.valueOf(100D)).intValue();
        WxPayOrderRespVO respVO = new WxPayOrderRespVO();
        respVO.setPrice(totalPrice);
        int payPrice = totalPrice;//需要支付的钱 默认等于订单金额
        if (roomInfoDO.getDeposit().compareTo(BigDecimal.ZERO) != 0) {
            //需要押金
            if (!ObjectUtils.isEmpty(payType)) {
                //余额支付或者团购支付  只需支付押金
                if (payType.compareTo(AppEnum.order_pay_type.WALLET.getValue()) == 0 || payType.compareTo(AppEnum.order_pay_type.TUANGOU.getValue()) == 0) {
                    payPrice = roomInfoDO.getDeposit().multiply(BigDecimal.valueOf(100D)).intValue();
                }
//                else if (payType.compareTo(AppEnum.order_pay_type.PKG.getValue()) == 0 && pkgInfoDO.getBalanceBuy()) {
//                    //套餐支付  并且套餐支持余额支付   只需支付押金
//                    payPrice = roomInfoDO.getDeposit().multiply(BigDecimal.valueOf(100D)).intValue();
//                }
            }
        }
        respVO.setPayPrice(payPrice);
        respVO.setOrderNo(orderNo);
        if (wxpay) {
            if (payPrice > 0) {
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
                try {
                    WxPayMpOrderResult wxPayMpOrderResult = myWxService.createOrder(wxPayService, roomInfoDO.getStoreId(), orderNo, payPrice, openId);
                    respVO.setPkg(wxPayMpOrderResult.getPackageValue());
                    respVO.setAppId(wxPayMpOrderResult.getAppId());
                    respVO.setNonceStr(wxPayMpOrderResult.getNonceStr());
                    respVO.setPaySign(wxPayMpOrderResult.getPaySign());
                    respVO.setSignType("MD5");
                    respVO.setTimeStamp(wxPayMpOrderResult.getTimeStamp());
                } catch (Exception e) {
                    e.printStackTrace();
//                throw new RuntimeException(e);
                    throw exception(USER_WEIXIN_PAY_ERROR);
                }
                if (null == ignoreOrderId) {
                    payOrderService.create(getLoginUserId(), orderNo, null, roomInfoDO.getStoreId(), payType, "房间预定订单", payPrice);
                } else {
                    payOrderService.create(getLoginUserId(), orderNo, ignoreOrderId, roomInfoDO.getStoreId(), payType, "续费订单", payPrice);
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
                        , ObjectUtils.isEmpty(pkgInfoDO) ? null : pkgInfoDO.getPkgId(), ignoreOrderId, payPrice, nightLong);
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
                throw exception(PKG_USE_STORE_ERROR);
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
//                if (!CollectionUtils.isAnyEmpty(weeksBetween) && weeksBetween.contains("7")) {
//                    weeksBetween.remove("7");
//                    weeksBetween.add("0");
//                }
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
            // 获取今天是星期几，其中星期天的值是1，星期六的值是7
            int dayOfWeek = startCal.get(Calendar.DAY_OF_WEEK);
            //加到结果
            // 将星期天的值1转换为7，其余依次减1
            if (dayOfWeek == Calendar.SUNDAY) {
                dayOfWeek = 7;
            } else {
                dayOfWeek--;
            }
            weekSet.add(String.valueOf(dayOfWeek));
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
            if (couponInfoDO.getStatus().intValue() != 0 || couponInfoDO.getExpriceTime().before(new Date())) {
                throw exception(COUPON_USED_ERROR);
            }
            //判断适用门店
            if (couponInfoDO.getStoreId().compareTo(storeId) != 0) {
                throw exception(COUPON_USE_CHECK_STORE_ERROR);
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
        if (nightLong || title.indexOf("通宵") != -1) {
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
                || title.indexOf("全场通用") != -1
                || title.indexOf("全场畅玩") != -1
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
        //说明一下  只有不存在微信付款时，才直接调用此接口
        //如果是微信付款的  那么是由支付回调来调用的此接口
        if (ObjectUtils.isEmpty(reqVO.getUserId())) {
            reqVO.setUserId(getLoginUserId());
        }
        //定义一些参数 备用
        String orderNo = reqVO.getOrderNo();
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
        int deposit = roomInfoDO.getDeposit().multiply(new BigDecimal(100.0)).intValue();
        CouponInfoDO couponInfoDO = null;
        if (!ObjectUtils.isEmpty(reqVO.getCouponId())) {
            couponInfoDO = couponInfoMapper.selectById(reqVO.getCouponId());
        }
        PkgInfoDO pkgInfoDO = null;
        if (!ObjectUtils.isEmpty(reqVO.getPkgId())) {
            pkgInfoDO = pkgInfoMapper.selectById(reqVO.getPkgId());
        }
        //下单之前仍然再检查一遍 并计算出应付总金额
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(reqVO.getUserId(), reqVO.getPayType(), reqVO.getRoomId(), reqVO.getStartTime(), reqVO.getEndTime(),
                couponInfoDO, pkgInfoDO, null, reqVO.getNightLong(), false);
        BigDecimal totalPrice = new BigDecimal(String.valueOf(wxPayOrderRespVO.getPayPrice() / 100.0));
        BigDecimal oldPrice = new BigDecimal(String.valueOf(wxPayOrderRespVO.getPayPrice() / 100.0));
        //判断是否有填团购券
        Integer groupType = null;
        BigDecimal groupPrice = BigDecimal.ZERO;
        GroupPayInfoDO groupPayInfoDO = null;
        if (!ObjectUtils.isEmpty(reqVO.getGroupPayNo())) {
            StoreInfoDO storeInfoDO = storeInfoMapper.selectById(roomInfoDO.getStoreId());
            //设置订单的支付类型为团购
            reqVO.setPayType(AppEnum.order_pay_type.TUANGOU.getValue());
            //处理掉中间有空格的情况
            reqVO.setGroupPayNo(reqVO.getGroupPayNo().replaceAll(" ", ""));
            //查询券信息
            IotGroupPayPrepareRespVO prepare = groupPayInfoService.prepare(roomInfoDO.getStoreId(), reqVO.getGroupPayNo());
            //校验券合法性
            checkGroupNo(prepare.getTicketName(), reqVO.getStartTime(), reqVO.getEndTime(), roomInfoDO.getType(), reqVO.getNightLong(), storeInfoDO.getTxStartHour(), storeInfoDO.getTxHour());
            //把券使用了
            groupPayInfoDO = groupPayInfoService.consume(roomInfoDO.getStoreId(), reqVO.getGroupPayNo(), prepare);
            //团购消费的  支付价格设置为0
            totalPrice = BigDecimal.ZERO;
            if (deposit > 0) {
                //需要押金 检查支付结果 这里注意：只检查押金是否支付
                try {
                    payOrderService.checkWxOrder(reqVO.getOrderNo(), roomInfoDO.getStoreId(), deposit);
                } catch (Exception e) {
                    //如果押金没有支付  撤销团购验券
                    groupPayInfoService.revoke(roomInfoDO.getStoreId(), prepare.getGroupPayType(), reqVO.getGroupPayNo(), groupPayInfoDO.getTicketInfo());
                    throw e;
                }
            }
        } else {
            //非团购支付
            //判断使用优惠券的情况
            if (!ObjectUtils.isEmpty(reqVO.getCouponId())) {
                //use
                couponInfoMapper.updateById(new CouponInfoDO().setCouponId(couponInfoDO.getCouponId()).setStatus(AppEnum.coupon_status.USED.getValue()));
            }
            //订单价格为0  就不需要扣费了
            if (totalPrice.compareTo(BigDecimal.ZERO) > 0) {
                switch (reqVO.getPayType()) {
                    case 1:
                    case 4:
                        //微信  直接检查支付状态    费用里面已经包含了押金
                        payOrderService.checkWxOrder(reqVO.getOrderNo(), roomInfoDO.getStoreId(), wxPayOrderRespVO.getPayPrice());
                        break;
                    case 2://余额
                        if (deposit > 0) {
                            //需要押金 检查支付结果
                            payOrderService.checkWxOrder(reqVO.getOrderNo(), roomInfoDO.getStoreId(), deposit);
                        }
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
        orderInfoDO.setEndTime(reqVO.getEndTime());
        //处理加时券
        if (!ObjectUtils.isEmpty(couponInfoDO) && couponInfoDO.getType().compareTo(AppEnum.coupon_type.JIASHI.getValue()) == 0) {
            orderInfoDO.setEndTime(new Date(orderInfoDO.getEndTime().getTime() + 1000 * 60 * 60 * couponInfoDO.getPrice().intValue()));
        }
        orderInfoDO.setNightLong(reqVO.getNightLong());
        orderInfoDO.setPrice(oldPrice);
        orderInfoDO.setDeposit(roomInfoDO.getDeposit());
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
        if (!ObjectUtils.isEmpty(reqVO.getGroupPayNo())) {
            //把订单id更新到团购券
            groupPayInfoMapper.updateById(new GroupPayInfoDO().setId(groupPayInfoDO.getId()).setOrderId(orderInfoDO.getOrderId()));
            //异步发送微信通知
            workWxService.sendOrderMsg(roomInfoDO.getStoreId(), reqVO.getUserId(), roomInfoDO.getRoomName(), groupPrice, null, null, reqVO.getPayType(), orderInfoDO.getGroupPayType(), orderNo, orderInfoDO.getStartTime(), orderInfoDO.getEndTime());
        } else {
            //异步发送微信通知
            workWxService.sendOrderMsg(roomInfoDO.getStoreId(), reqVO.getUserId(), roomInfoDO.getRoomName(), totalPrice, couponInfoDO, pkgInfoDO, reqVO.getPayType(), orderInfoDO.getGroupPayType(), orderNo, orderInfoDO.getStartTime(), orderInfoDO.getEndTime());
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
        //结束时间 等于
        Date endTime = reqVO.getEndTime();
        log.info("订单:{},续费开始时间:{}，结束时间：{}", orderInfoDO.getOrderNo(), startTime, endTime);
        if (startTime.compareTo(endTime) == 0) {
            //续费结束时间等于开始时间  退款
            throw exception(ORDER_RENEW_TIME_ERROR);
        }
        CouponInfoDO couponInfoDO = null;
        if (!ObjectUtils.isEmpty(reqVO.getCouponId())) {
            couponInfoDO = couponInfoMapper.selectById(reqVO.getCouponId());
        }
        WxPayOrderRespVO wxPayOrderRespVO = preOrder(userId, null, orderInfoDO.getRoomId(), startTime, endTime, couponInfoDO, null, reqVO.getOrderId(), false, false);
        //订单价格
        BigDecimal totalPrice = new BigDecimal(String.valueOf(wxPayOrderRespVO.getPayPrice() / 100.0));
        switch (reqVO.getPayType()) {
            case 1://微信
                if (wxPayOrderRespVO.getPayPrice() > 0) {
                    payOrderService.checkWxOrder(reqVO.getOrderNo(), roomInfoDO.getStoreId(), wxPayOrderRespVO.getPayPrice());
                    //是微信支付的  增加已支付的金额
                    orderInfoDO.setPayPrice(orderInfoDO.getPayPrice().add(totalPrice));
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
        //如果使用了优惠券  则使用掉
        if (!ObjectUtils.isEmpty(reqVO.getCouponId())) {
            //use
            couponInfoMapper.updateById(new CouponInfoDO().setCouponId(couponInfoDO.getCouponId()).setStatus(AppEnum.coupon_status.USED.getValue()));
        }
        //支付完了，增加订单的结束时间
        orderInfoDO.setEndTime(endTime);
        //处理加时券
        if (!ObjectUtils.isEmpty(couponInfoDO) && couponInfoDO.getType().compareTo(AppEnum.coupon_type.JIASHI.getValue()) == 0) {
            orderInfoDO.setEndTime(new Date(orderInfoDO.getEndTime().getTime() + 1000 * 60 * 60 * couponInfoDO.getPrice().intValue()));
        }
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
        workWxService.sendRenewMsg(roomInfoDO.getStoreId(), userId, roomInfoDO.getRoomName(), totalPrice, reqVO.getPayType(), orderInfoDO.getOrderNo(), orderInfoDO.getEndTime(), couponInfoDO, false);
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
            //如果有空调控制器，设置一下
            if (deviceService.countKongtiao(orderInfo.getRoomId()) > 0) {
                orderInfo.setKongtiaoCount(1);
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
            //只能更换到小于等于当前房间级别的房间
            RoomInfoDO newRoomInfo = roomInfoMapper.selectById(roomId);
            RoomInfoDO oldRoomInfo = roomInfoMapper.selectById(orderInfoDO.getRoomId());
            if (newRoomInfo.getType().compareTo(oldRoomInfo.getType()) > 0) {
                throw exception(ORDER_CHANGE_ROOM_ERROR);
            } else {
                //两个房间押金必须一样 不然会出现退错押金的问题
                if (newRoomInfo.getDeposit().compareTo(oldRoomInfo.getDeposit()) != 0) {
                    throw exception(ORDER_CHANGE_ROOM_ERROR);
                }
                //检查是否可用
                preOrder(loginUserId, null, roomId, orderInfoDO.getStartTime(), orderInfoDO.getEndTime(), null, null, null, false, false);
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
                groupPayInfoService.revoke(groupPayInfoDO.getStoreId(), groupPayInfoDO.getGroupPayType(), groupPayInfoDO.getGroupNo(), groupPayInfoDO.getTicketInfo());
                //删除团购券记录
                groupPayInfoMapper.deleteById(groupPayInfoDO.getId());
            } else {
                orderInfoDO.setRefundPrice(orderInfoDO.getPayPrice());
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
            //进行微信退款 （微信支付押金或微信续费的金额）
            payOrderService.refundByOrder(orderInfoDO.getOrderId(), orderInfoDO.getOrderNo(), orderInfoDO.getStoreId());
            //进行余额退款 （下单或续费的金额）
            payOrderService.refundBalance(orderInfoDO.getStoreId(), orderInfoDO.getOrderNo(), orderInfoDO.getUserId());
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
                    preOrder(loginUserId, null, orderInfoDO.getRoomId(), now, orderInfoDO.getEndTime(), null, null, orderId, false, false);
                }
            }
            //开始订单
            orderInfoDO.setStatus(AppEnum.order_status.START.getValue());
            orderInfoMapper.updateById(orderInfoDO);
            //房间改为进行中
            roomInfoMapper.updateStatusById(AppEnum.room_status.USED.getValue(), orderInfoDO.getRoomId());
            //异步延时发送欢迎语
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.schedule(() -> {
                // 这是异步执行的任务
                deviceService.runSound(orderInfoDO.getRoomId(), 1);
            }, 40, TimeUnit.SECONDS); // 延迟40秒后执行任务

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
        List<OrderListJobVO> orderList = orderInfoMapper.getListByJob();
        if (!CollectionUtils.isAnyEmpty(orderList)) {
            Set<Long> startRoomIds = new HashSet<>();
            Set<Long> endRoomIds = new HashSet<>();
            Set<Long> clearRoomIds = new HashSet<>();
            Set<Long> jumpClearRoomIds = new HashSet<>();
            Set<Long> startOrderIds = new HashSet<>();
            Set<Long> endOrderIds = new HashSet<>();
            List<ClearInfoDO> clearInfoDOList = new ArrayList<>();
            //延时异步执行
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
            //按照门店分组，因为不同的门店，有不同的规则
            Map<Long, List<OrderListJobVO>> listByStoreId = orderList.stream().collect(Collectors.groupingBy(x -> x.getStoreId()));
            listByStoreId.entrySet().forEach(v -> {
                //先查询出门店信息 以读取配置
                StoreInfoDO storeInfoDO = storeInfoMapper.selectById(v.getKey());
                v.getValue().forEach(x -> {
                    if (x.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
                        //未开始 达到预订时间后，就开始订单
                        if (x.getStartTime().before(now)) {
                            startRoomIds.add(x.getRoomId());
                            startOrderIds.add(x.getOrderId());
                            //如果房间类型是台球，那么直接开电
                            if (x.getRoomClass().compareTo(AppEnum.room_class.TAIQIU.getValue()) == 0) {
                                deviceService.openRoomDoor(null, x.getStoreId(), x.getRoomId(), 4);
                            }
                            //异步延时播放欢迎语
                            executorService.schedule(() -> {
                                deviceService.runSound(x.getRoomId(), 1);
                            }, 40, TimeUnit.SECONDS);
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
                                endOrderIds.add(x.getOrderId());
                                endRoomIds.add(x.getRoomId());
                                //添加保洁记录  如果房间不需要保洁  那就跳过
                                if (!x.getJumpClear()) {
                                    clearRoomIds.add(x.getRoomId());
                                    ClearInfoDO clearInfoDO = new ClearInfoDO();
                                    clearInfoDO.setOrderId(x.getOrderId()).setStoreId(x.getStoreId()).setOrderNo(x.getOrderNo()).setRoomId(x.getRoomId());
                                    clearInfoDOList.add(clearInfoDO);
                                } else {
                                    jumpClearRoomIds.add(x.getRoomId());
                                }
                            } else {
                                //检查距离结束的时间，发送语音提醒
                                long minutes = Math.abs(ChronoUnit.MINUTES.between(now.toInstant(), x.getEndTime().toInstant()));
                                if (minutes == 30) {
                                    deviceService.runSound(x.getRoomId(), 2);
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
                        } catch (Exception e) {
                            //异常时不影响其他订单关闭
                            log.error(e.getMessage());
                        }
                    } else if (x.getStatus().compareTo(AppEnum.order_status.FINISH.getValue()) == 0) {
                        //已完成  主要是处理延时关电的  以及进行押金退款
                        long minutes = Math.abs(ChronoUnit.MINUTES.between(now.toInstant(), x.getEndTime().toInstant()));
                        //完成后第5分钟再处理  避免与设置的订单结束后5分钟才能预订起冲突
                        if (minutes == 5) {
                            //押金退款
                            if (x.getDeposit().compareTo(BigDecimal.ZERO) != 0) {
                                payOrderService.refundDeposit(x.getOrderNo(), x.getDeposit().multiply(BigDecimal.valueOf(100.0)).intValue());
                            }
                            //处理延时关灯 如果店铺不需要延时关电，就不处理了
                            try {
                                if (storeInfoDO.getDelayLight()) {
                                    //如果有正在进行的订单也是不允许关的
                                    if (orderInfoMapper.countByRoomCurrent(x.getRoomId(), null) == 0) {
                                        deviceService.closeLightByRoomId(null, x.getStoreId(), x.getRoomId(), 4);
                                    }
                                }
                            } catch (Exception e) {
                                //异常时不影响其他订单关闭
                                log.error(e.getMessage());
//                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
            });
            // 关闭ExecutorService，不再接受新任务，已经提交的任务将继续执行完毕
            executorService.shutdown();
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
                if (!CollectionUtils.isAnyEmpty(jumpClearRoomIds)) {
                    //批量修改房间状态为空闲
                    roomInfoMapper.updateStatusByIds(AppEnum.room_status.ENABLE.getValue(), jumpClearRoomIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                }
                if (!CollectionUtils.isAnyEmpty(clearRoomIds)) {
                    //批量修改房间状态为待清洁
                    roomInfoMapper.updateStatusByIds(AppEnum.room_status.CLEAR.getValue(), clearRoomIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                    //取消掉这些房间存在的历史保洁订单
                    clearInfoMapper.cancelByRoomIds(clearRoomIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                }
                if (!CollectionUtils.isAnyEmpty(endOrderIds)) {
                    //批量修改订单状态为已完成
                    orderInfoMapper.updateStatusByIds(AppEnum.order_status.FINISH.getValue(), endOrderIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
                    //然后再新增本次的保洁订单
                    clearInfoMapper.insertBatch(clearInfoDOList);
                    //发送订单结束的微信通知
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
            sb.append("订单已结束通知\n");
            sb.append(">门店名称:").append(vo.getStoreName()).append("\n");
            sb.append(">房间名称:").append(vo.getRoomName()).append("\n");
            sb.append(">时间:").append(dateStr).append("\n");
            workWxService.sendClearMsg(vo.getOrderWebhook(), sb.toString());
        }
    }

    @Async
    protected void sendClearMsg(Long roomId) {
        String dateStr = DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);
        AppRoomListVO vo = roomInfoMapper.getInfoById(roomId);
        StringBuffer sb = new StringBuffer();
        sb.append("订单已结束通知\n");
        sb.append(">门店名称:").append(vo.getStoreName()).append("\n");
        sb.append(">房间名称:").append(vo.getRoomName()).append("\n");
        sb.append(">时间:").append(dateStr).append("\n");
        workWxService.sendClearMsg(vo.getOrderWebhook(), sb.toString());
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
                long l1 = (orderInfoDO.getStartTime().getTime() - now.getTime()) / 1000 / 60;
                if (l1 > roomInfoDO.getLeadHour() * 60) {
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

    @Override
    @Transactional
    public void closeOrder(Long orderId) {
        //提前结束订单  只有进行中的订单才能提前结束  订单截止时间设置为现在   并触发一下关电
        OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
        if (!ObjectUtils.isEmpty(orderInfoDO) && orderInfoDO.getUserId().compareTo(getLoginUserId()) == 0) {
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                orderInfoMapper.updateById(new OrderInfoDO().setOrderId(orderId).setEndTime(new Date()).setStatus(AppEnum.order_status.FINISH.getValue()));
                RoomInfoDO roomInfoDO = roomInfoMapper.selectById(orderInfoDO.getRoomId());
                if (!roomInfoDO.getJumpClear()) {
                    //取消掉这些房间存在的历史保洁订单
                    clearInfoMapper.cancelByRoomId(orderInfoDO.getRoomId());
                    //然后再新增本次的保洁订单
                    ClearInfoDO clearInfoDO = new ClearInfoDO().setOrderId(orderInfoDO.getOrderId()).setStoreId(orderInfoDO.getStoreId())
                            .setOrderNo(orderInfoDO.getOrderNo()).setRoomId(orderInfoDO.getRoomId());
                    clearInfoMapper.insert(clearInfoDO);
                    //发送需要保洁的微信通知
                    sendClearMsg(orderInfoDO.getRoomId());
                }
                //发送用户提前结束订单通知
                workWxService.sendCloseOrderMsg(orderInfoDO.getStoreId(), getLoginUserId(), orderInfoDO.getRoomId(), orderInfoDO.getPayType(), orderInfoDO.getGroupPayType(), orderInfoDO.getOrderNo());
                deviceService.closeRoomDoor(getLoginUserId(), orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), 1);
                flushRoomStatus(orderInfoDO.getRoomId());
            } else {
                throw exception(ADMIN_ORDER_OPRATION_ERROR);
            }
        } else {
            throw exception(OPRATION_ERROR);
        }


    }

    @Override
    public String preGroupNo(PreGroupNoReqVO reqVO) {
        //处理掉中间有空格的情况
        reqVO.setCode(reqVO.getCode().replaceAll(" ", ""));
        IotGroupPayPrepareRespVO prepare = groupPayInfoService.prepare(reqVO.getStoreId(), reqVO.getCode());
        return prepare.getTicketName();
    }

    @Override
    @Transactional
    public void controlKT(ControlKTReqVO reqVO) {
        //找出订单
        OrderInfoDO orderInfoDO = orderInfoMapper.selectOne(new LambdaQueryWrapperX<OrderInfoDO>().eq(OrderInfoDO::getOrderKey, reqVO.getOrderKey()));
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                deviceService.controlKT(reqVO.getCmd(), orderInfoDO.getStoreId(), orderInfoDO.getRoomId());
            } else {
                throw exception(CLEAR_ORDER_STATUS_ERROR);
            }
        } else {
            throw exception(ORDER_NOT_FOUND_ERROR);
        }
    }

}
