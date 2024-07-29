package com.yanzu.module.member.service.user;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.common.annotations.VisibleForTesting;
import com.yanzu.framework.common.enums.CommonStatusEnum;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.LoginUser;
import com.yanzu.framework.security.core.util.SecurityFrameworkUtils;
import com.yanzu.framework.tenant.core.context.TenantContextHolder;
import com.yanzu.module.infra.api.file.FileApi;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderInfo;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.user.vo.*;
import com.yanzu.module.member.convert.franchiseinfo.FranchiseInfoConvert;
import com.yanzu.module.member.convert.user.UserConvert;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import com.yanzu.module.member.dal.mysql.couponinfo.CouponInfoMapper;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;
import com.yanzu.module.member.dal.mysql.franchiseinfo.FranchiseInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.enums.AppWxPayTypeEnum;
import com.yanzu.module.member.service.order.AppOrderService;
import com.yanzu.module.member.service.payorder.PayOrderService;
import com.yanzu.module.member.service.wx.MyWxService;
import com.yanzu.module.member.service.wx.WorkWxService;
import com.yanzu.module.system.api.sms.SmsCodeApi;
import com.yanzu.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import com.yanzu.module.system.api.social.SocialUserApi;
import com.yanzu.module.system.api.tenant.TenantApi;
import com.yanzu.module.system.enums.sms.SmsSceneEnum;
import com.yanzu.module.system.enums.social.SocialTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getTenantId;
import static com.yanzu.module.member.enums.AppEnum.WX_PAY_ORDER;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 会员 User Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Valid
@Slf4j
public class AppUserServiceImpl implements AppUserService {

    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private FileApi fileApi;
    @Resource
    private SmsCodeApi smsCodeApi;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private StoreUserMapper storeUserMapper;

    @Resource
    private CouponInfoMapper couponInfoMapper;

    @Resource
    private UserMoneyBillMapper userMoneyBillMapper;

    @Resource
    private TenantApi tenantApi;

    @Resource
    private FranchiseInfoMapper franchiseInfoMapper;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private DiscountRulesMapper discountRulesMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private SocialUserApi socialUserApi;

    @Value("${wx.pay.returnUrl}")
    private String returnUrl;

    @Autowired
    private MyWxService myWxService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private WorkWxService workWxService;

    @Resource
    private AppOrderService appOrderService;


    @Override
    public MemberUserDO getUserByMobile(String mobile) {
        return memberUserMapper.selectByMobile(mobile);
    }

    @Override
    public List<MemberUserDO> getUserListByNickname(String nickname) {
        return memberUserMapper.selectListByNicknameLike(nickname);
    }

    @Override
    public MemberUserDO createUserIfAbsent(String mobile, String registerIp) {
        // 用户已经存在
        MemberUserDO user = memberUserMapper.selectByMobile(mobile);
        if (user != null) {
            return user;
        }
        // 用户不存在，则进行创建
        return this.createUser(mobile, registerIp);
    }

    private MemberUserDO createUser(String mobile, String registerIp) {
        // 生成密码
        String password = IdUtil.fastSimpleUUID();
        // 插入用户
        MemberUserDO user = new MemberUserDO();
        user.setNickname("用户" + mobile.substring(5, 11));
        user.setMobile(mobile);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(encodePassword(password)); // 加密密码
        user.setRegisterIp(registerIp);
        user.setUserType(AppEnum.member_user_type.MEMBER.getValue());//默认都是用户
        memberUserMapper.insert(user);
        return user;
    }

    @Override
    public void updateUserLogin(Long id, String loginIp) {
        memberUserMapper.updateById(new MemberUserDO().setId(id).setLoginIp(loginIp).setLoginDate(LocalDateTime.now()));
    }

    @Override
    public void updateUserType(Long id, Integer userType) {
        memberUserMapper.updateById(new MemberUserDO().setId(id).setUserType(userType));
    }

    @Override
    public MemberUserDO getUser(Long id) {
        return memberUserMapper.selectById(id);
    }

    @Override
    public List<MemberUserDO> getUserList(Collection<Long> ids) {
        return memberUserMapper.selectBatchIds(ids);
    }

    @Override
    public void updateUserNickname(Long userId, String nickname) {
        MemberUserDO user = this.checkUserExists(userId);
        // 仅当新昵称不等于旧昵称时进行修改
        if (nickname.equals(user.getNickname())) {
            return;
        }
        MemberUserDO userDO = new MemberUserDO();
        userDO.setId(user.getId());
        userDO.setNickname(nickname);
        memberUserMapper.updateById(userDO);
    }

    @Override
    public String updateUserAvatar(Long userId, InputStream avatarFile) throws Exception {
        this.checkUserExists(userId);
        // 创建文件
        String avatar = fileApi.createFile(IoUtil.readBytes(avatarFile));
        // 更新头像路径
        memberUserMapper.updateById(MemberUserDO.builder().id(userId).avatar(avatar).build());
        return avatar;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserMobile(Long userId, AppUserUpdateMobileReqVO reqVO) {
        // 检测用户是否存在
        checkUserExists(userId);
        // TODO 芋艿：oldMobile 应该不用传递

        // 校验旧手机和旧验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(reqVO.getOldMobile()).setCode(reqVO.getOldCode()).setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));
        // 使用新验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(reqVO.getMobile()).setCode(reqVO.getCode()).setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));

        // 更新用户手机
        memberUserMapper.updateById(MemberUserDO.builder().id(userId).mobile(reqVO.getMobile()).build());
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public AppUserInfoRespVO getUserInfo(Long loginUserId) {
        MemberUserDO memberUserDO = memberUserMapper.selectById(loginUserId);
        AppUserInfoRespVO respVO = UserConvert.INSTANCE.convert(memberUserDO);
        //查询余额
        StoreUserDO storeUserDO = storeUserMapper.getTotalBalance(loginUserId);
        if (ObjectUtils.isEmpty(storeUserDO)) {
            respVO.setBalance(BigDecimal.ZERO);
            respVO.setGiftBalance(BigDecimal.ZERO);
        } else {
            respVO.setBalance(storeUserDO.getBalance());
            respVO.setGiftBalance(storeUserDO.getGiftBalance());
        }
        //查询可用优惠券数量
        respVO.setCouponCount(couponInfoMapper.countByUserId(loginUserId));
        return respVO;
    }

    @Override
    public PageResult<AppUserMoneyBillRespVO> getBalancePage(AppUserMoneyBillPageReqVO reqVO) {
        reqVO.setUserId(getLoginUserId());
        IPage<AppUserMoneyBillRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        userMoneyBillMapper.getBalancePage(page, reqVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<AppGiftBalanceListRespVO> getGiftBalanceList() {
        return storeUserMapper.getGiftBalanceList(getLoginUserId());
    }

    @Override
    @Transactional
    public void eechargeBalance(AppRechargeBalanceReqVO reqVO) {
//        reqVO.setUserId(getLoginUserId());
        payOrderService.checkWxOrder(reqVO.getOrderNo(), reqVO.getStoreId(), reqVO.getPrice());
        //增加账户余额
        BigDecimal addMoney = new BigDecimal(String.valueOf(reqVO.getPrice() / 100.0));
        if (addMoney.compareTo(BigDecimal.ZERO) <= 0) {
            throw exception(OPRATION_ERROR);
        }
        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(reqVO.getUserId(), reqVO.getStoreId());
        if (ObjectUtils.isEmpty(storeUserDO)) {
            //如果是会员第一次在门店充值，关系可能是不存在的 要先添加关系
            storeUserDO = new StoreUserDO();
            storeUserDO.setStoreId(reqVO.getStoreId());
            storeUserDO.setUserId(reqVO.getUserId());
            storeUserDO.setType(AppEnum.member_user_type.MEMBER.getValue());
            storeUserDO.setBalance(BigDecimal.ZERO);
            storeUserDO.setGiftBalance(BigDecimal.ZERO);
            storeUserMapper.insert(storeUserDO);
        }
        storeUserDO.setBalance(storeUserDO.getBalance().add(addMoney));
        //增加余额充值明细
        UserMoneyBillDO userMoneyBillDO = new UserMoneyBillDO();
        userMoneyBillDO.setStoreId(reqVO.getStoreId());
        userMoneyBillDO.setMoney(addMoney);
        userMoneyBillDO.setUserId(reqVO.getUserId());
        userMoneyBillDO.setRemark("在线余额充值");
        userMoneyBillDO.setMoneyType(AppEnum.user_money_type.MONEY.getValue());
        userMoneyBillDO.setTotalMoney(storeUserDO.getBalance());
        userMoneyBillDO.setType(AppEnum.user_money_bill_type.RECHARGE.getValue());
        userMoneyBillMapper.insert(userMoneyBillDO);
        //增加赠送余额 先查询出该门店，该充值金额的最大赠送金额
        BigDecimal gift = discountRulesMapper.getMaxGiftByStoreIdAndPrice(reqVO.getStoreId(), addMoney);
        log.info("用户:{},充值门店:{},充值:{}元，赠送:{}元", reqVO.getUserId(), reqVO.getStoreId(), addMoney, gift);
        if (!ObjectUtils.isEmpty(gift) && gift.compareTo(BigDecimal.ZERO) > 0) {
            storeUserDO.setGiftBalance(storeUserDO.getGiftBalance().add(gift));
            userMoneyBillDO = new UserMoneyBillDO();
            userMoneyBillDO.setStoreId(reqVO.getStoreId());
            userMoneyBillDO.setMoney(gift);
            userMoneyBillDO.setUserId(reqVO.getUserId());
            userMoneyBillDO.setRemark("充值余额赠送");
            userMoneyBillDO.setMoneyType(AppEnum.user_money_type.GIFT_MONEY.getValue());
            userMoneyBillDO.setTotalMoney(storeUserDO.getGiftBalance());
            userMoneyBillDO.setType(AppEnum.user_money_bill_type.GIFT.getValue());
            userMoneyBillMapper.insert(userMoneyBillDO);
        }
        storeUserMapper.updateById(storeUserDO);
        workWxService.sendRechargeMsg(reqVO.getStoreId(), reqVO.getUserId(), addMoney, gift);
    }

    @Override
    public AppFranchiseInfoRespVO getFranchiseInfo(HttpServletRequest request) {
        Long tenantId = getTenantId(request);
        String adminPhone = tenantApi.getAdminPhone(tenantId);
        FranchiseInfoDO franchiseInfoDO = franchiseInfoMapper.getByUserId(getLoginUserId());
        AppFranchiseInfoRespVO respVO = new AppFranchiseInfoRespVO();
        respVO.setFranchise(adminPhone);
        respVO.setIsCommit(!ObjectUtils.isEmpty(franchiseInfoDO));
        return respVO;
    }

    @Override
    @Transactional
    public void saveFranchiseInfo(AppFranchiseInfoReqVO reqVO) {
        FranchiseInfoDO franchiseInfoDO = franchiseInfoMapper.getByUserId(getLoginUserId());
        if (ObjectUtils.isEmpty(franchiseInfoDO)) {
            FranchiseInfoDO newfranchiseInfoDO = FranchiseInfoConvert.INSTANCE.convert2(reqVO);
            newfranchiseInfoDO.setUserId(getLoginUserId());
            franchiseInfoMapper.insert(newfranchiseInfoDO);
        }
    }

    @Override
    public PageResult<AppCouponPageRespVO> getCouponPage(AppCouponPageReqVO reqVO) {
        reqVO.setUserId(getLoginUserId());
        IPage<AppCouponPageRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        if (ObjectUtils.isEmpty(reqVO.getRoomId())) {
            //个人中心分页查询
            couponInfoMapper.getCouponPage(page, reqVO);
            return new PageResult<>(page.getRecords(), page.getTotal());
        } else {
            //提交订单页查询
            //下单的时候  要返回可用状态
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
            StoreInfoDO storeInfoDO = storeInfoMapper.selectById(roomInfoDO.getStoreId());
            //如果该用户，在本店铺是新用户，就送他一张新人加时券,一个月有效
//            int cCount = appOrderService.countNewUserByStoreId(reqVO.getUserId(), storeInfoDO.getStoreId());
//            if (couponInfoMapper.countNewUserByStoreId(reqVO.getUserId(), storeInfoDO.getStoreId()) == 0) {
//                CouponInfoDO couponInfoDO = new CouponInfoDO();
//                couponInfoDO.setCouponName("新用户1小时加时券")
//                        .setStatus(0)
//                        .setPrice(BigDecimal.valueOf(1))
//                        .setMinUsePrice(BigDecimal.valueOf(1))
//                        .setUserId(reqVO.getUserId())
//                        .setCreateUserId(1L)
//                        .setStoreId(storeInfoDO.getStoreId())
//                        .setType(AppEnum.coupon_type.JIASHI.getValue())
//                        .setExpriceTime(Date.from(LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant()));
//                couponInfoMapper.insert(couponInfoDO);
//            }
            //先计算出订单价格
            BigDecimal mathPrice = appOrderService.mathPrice(roomInfoDO.getPrice(), roomInfoDO.getDeposit(), roomInfoDO.getWorkPrice(), storeInfoDO.getWorkPrice(),
                    roomInfoDO.getTongxiaoPrice(), storeInfoDO.getTxHour(), reqVO.getStartTime(), reqVO.getEndTime(), reqVO.getNightLong(), null, null);
            //再计算出时长 精确到小数点后两位
            BigDecimal hours = new BigDecimal(String.valueOf((reqVO.getEndTime().getTime() - reqVO.getStartTime().getTime()) / 1000.0 / 60 / 60)).setScale(2, BigDecimal.ROUND_HALF_UP);
            couponInfoMapper.getCouponPage(page, reqVO);
            //校验每张优惠券是否可用
            if (!CollectionUtils.isEmpty(page.getRecords())) {
                page.getRecords().stream().forEach(x -> {
                    boolean f1 = false;
                    if (x.getType().compareTo(AppEnum.coupon_type.DIKOU.getValue()) == 0 || x.getType().compareTo(AppEnum.coupon_type.JIASHI.getValue()) == 0) {
                        //抵扣或加时
                        f1 = hours.compareTo(x.getMinUsePrice()) >= 0;
                    } else if (x.getType().compareTo(AppEnum.coupon_type.MANJIAN.getValue()) == 0) {
                        //满减
                        f1 = mathPrice.compareTo(x.getMinUsePrice()) >= 0;
                    }
                    //判断门店
                    boolean f2 = x.getStoreId().compareTo(roomInfoDO.getStoreId()) == 0;
                    //判断房间类型的限制
                    boolean f3 = ObjectUtils.isEmpty(x.getRoomType()) || x.getRoomType().compareTo(roomInfoDO.getType()) == 0;
                    x.setEnable(f1 && f2 && f3);
                });
            }
            return new PageResult<>(page.getRecords().stream().sorted((x1, x2) -> String.valueOf(x2.isEnable())
                    .compareTo(String.valueOf(x1.isEnable()))).collect(Collectors.toList()), Long.valueOf(page.getRecords().size()));
        }

    }

    @Override
    @Transactional
    public void updateUserAvatarUrl(Long userId, String avatarUrl) {
        // 更新头像路径
        memberUserMapper.updateById(MemberUserDO.builder().id(userId).avatar(avatarUrl).build());
    }

    @Override
    @Transactional
    public WxPayOrderRespVO preRechargeBalance(AppPreRechargeBalanceReqVO reqVO) {
        reqVO.setUserId(getLoginUserId());
        String orderNo = getOrderNo();
        WxPayOrderRespVO respVO = new WxPayOrderRespVO();
        respVO.setPrice(reqVO.getPrice());
        respVO.setOrderNo(orderNo);
        if (reqVO.getPrice() > 0) {
            //需要微信下单  先获取到该用户的openId
            String openId = socialUserApi.getUserOpenIdByType(reqVO.getUserId(), SocialTypeEnum.WECHAT_MINI_APP.getType());
            if (ObjectUtils.isEmpty(openId)) {
                throw exception(AUTH_USER_BIND_MINIAPP_ERROR);
            }
            //创建微信支付实例
            WxPayService wxPayService = myWxService.initWxPay(reqVO.getStoreId());
            try {
                //创建订单
                WxPayMpOrderResult wxPayMpOrderResult = myWxService.createOrder(wxPayService, reqVO.getStoreId(), orderNo, reqVO.getPrice(), openId);
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
            payOrderService.create(reqVO.getUserId(), orderNo, null, reqVO.getStoreId(), AppEnum.order_pay_type.WEIXIN.getValue(), "余额充值订单", reqVO.getPrice());
            //把订单号存到redis 如果已经充值了 就移除这个订单号
            String redisKey = String.format(WX_PAY_ORDER, orderNo);
            Long tenantId = TenantContextHolder.getTenantId();
            // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
            if (tenantId == null) {
                LoginUser user = SecurityFrameworkUtils.getLoginUser();
                tenantId = user.getTenantId();
            }
            redisTemplate.opsForValue().set(redisKey, new WxPayOrderInfo(AppWxPayTypeEnum.RECHARGE, orderNo, reqVO.getUserId(), tenantId
                    , reqVO.getStoreId(), reqVO.getPrice()), 1, TimeUnit.DAYS);
        }
        return respVO;
    }

    @Override
    public BigDecimal getGiftBalance(Long storeId) {
        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(getLoginUserId(), storeId);
        if (ObjectUtils.isEmpty(storeUserDO)) {
            return BigDecimal.ZERO;
        } else {
            return storeUserDO.getGiftBalance();
        }
    }

    @Override
    public AppStoreBalanceRespVO getStoreBalance(Long storeId) {
        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(getLoginUserId(), storeId);
        if (ObjectUtils.isEmpty(storeUserDO)) {
            return new AppStoreBalanceRespVO(BigDecimal.ZERO, BigDecimal.ZERO);
        }
        return new AppStoreBalanceRespVO(storeUserDO.getBalance(), storeUserDO.getGiftBalance());
    }

    private String getOrderNo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDate = currentDateTime.format(dateFormatter);
        Random random = new Random();
        int randomNum = random.nextInt(100000000);
        String randomNumString = String.format("%08d", randomNum);
        return "CZ" + currentDate + randomNumString;
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @VisibleForTesting
    public MemberUserDO checkUserExists(Long id) {
        if (id == null) {
            return null;
        }
        MemberUserDO user = memberUserMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        return user;
    }


}
