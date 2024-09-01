package com.yanzu.module.member.service.pkg;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.LoginUser;
import com.yanzu.framework.security.core.util.SecurityFrameworkUtils;
import com.yanzu.framework.tenant.core.context.TenantContextHolder;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderInfo;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.pkg.vo.*;
import com.yanzu.module.member.dal.dataobject.pkginfo.PkgInfoDO;
import com.yanzu.module.member.dal.dataobject.pkguserinfo.PkgUserInfoDO;
import com.yanzu.module.member.dal.mysql.pkginfo.PkgInfoMapper;
import com.yanzu.module.member.dal.mysql.pkguserinfo.PkgUserInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.enums.AppWxPayTypeEnum;
import com.yanzu.module.member.service.payorder.PayOrderService;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import com.yanzu.module.member.service.wx.MyWxService;
import com.yanzu.module.system.api.social.SocialUserApi;
import com.yanzu.module.system.enums.social.SocialTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.AppEnum.WX_PAY_ORDER;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

@Service
@Validated
@Slf4j
public class PkgServiceImpl implements PkgService {

    @Resource
    private PkgInfoMapper pkgInfoMapper;

    @Resource
    private PkgUserInfoMapper pkgUserInfoMapper;

    @Resource
    private StoreUserMapper storeUserMapper;

    @Resource
    private StoreInfoService storeInfoService;

    @Resource
    private SocialUserApi socialUserApi;

    @Autowired
    private MyWxService myWxService;


    @Value("${wx.pay.returnUrl}")
    private String returnUrl;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public PageResult<AppAdminPkgPageRespVO> getAdminPkgPage(AppAdminPkgPageReqVO reqVO) {
        // 校验用户类型
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //仅限查看当前用户所在门店的
        String storeIds = storeUserMapper.getIdsByUserIdAndAdmin(getLoginUserId()).stream().collect(Collectors.joining(","));
        if (StringUtils.isEmpty(storeIds)) {
            return PageResult.empty();
        }
        IPage<AppAdminPkgPageRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        pkgInfoMapper.getAdminPkgPage(page, reqVO, storeIds);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    @Transactional
    public void saveAdminPkg(AppAdminPkgSaveReqVO reqVO) {
        // 校验用户类型
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //排序可用时间和星期
        if (!ObjectUtils.isEmpty(reqVO.getEnableTime())) {
            reqVO.setEnableTime(reqVO.getEnableTime().stream().sorted().collect(Collectors.toList()));
        }
        if (!ObjectUtils.isEmpty(reqVO.getEnableWeek())) {
            reqVO.setEnableWeek(reqVO.getEnableWeek().stream().sorted().collect(Collectors.toList()));
        }
        if (ObjectUtils.isEmpty(reqVO.getPkgId())) {
            //新增
            PkgInfoDO pkgInfoDO = new PkgInfoDO();
            BeanUtils.copyProperties(reqVO, pkgInfoDO);
            pkgInfoDO.setCreateUserId(getLoginUserId());
            pkgInfoMapper.insert(pkgInfoDO);
        } else {
            PkgInfoDO pkgInfoDO = pkgInfoMapper.selectById(reqVO.getPkgId());
            if (ObjectUtils.isEmpty(pkgInfoDO)) {
                throw exception(OPRATION_ERROR);
            }
            BeanUtils.copyProperties(reqVO, pkgInfoDO);
            pkgInfoMapper.updateById(pkgInfoDO);
        }
    }


    @Override
    @Transactional
    public void enable(Long pkgId) {
        // 校验用户类型
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        PkgInfoDO pkgInfoDO = pkgInfoMapper.selectById(pkgId);
        if (ObjectUtils.isEmpty(pkgInfoDO)) {
            throw exception(OPRATION_ERROR);
        }
        pkgInfoMapper.updateById(new PkgInfoDO().setPkgId(pkgId).setEnable(!pkgInfoDO.getEnable()));
    }

    @Override
    public PageResult<AppPkgPageRespVO> getPkgPage(AppPkgPageReqVO reqVO) {
        IPage<AppPkgPageRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        pkgInfoMapper.getPkgPage(page, reqVO);
        List<AppPkgPageRespVO> respVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            if (!ObjectUtils.isEmpty(reqVO.getStartTime()) && !ObjectUtils.isEmpty(reqVO.getEndTime())){
                //订单时长 （分钟）
                long orderMinutes = Math.abs(ChronoUnit.MINUTES.between(reqVO.getStartTime().toInstant(), reqVO.getEndTime().toInstant()));
                for (AppPkgPageRespVO x : page.getRecords()) {
                    //删除时长不匹配的套餐
                    if (orderMinutes == x.getHours() * 60) {
                        respVOList.add(x);
                    }
                }
            }else{
                //不限制订单时间  就返回所有
                respVOList=page.getRecords();
            }
        }
        return new PageResult<>(respVOList, page.getTotal());
    }

    @Override
    public PageResult<AppPkgMyPageRespVO> getMyPkgPage(AppMyPkgPageReqVO reqVO) {
        IPage<AppPkgMyPageRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        pkgInfoMapper.getMyPkgPage(page, reqVO, getLoginUserId());
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    @Transactional
    public WxPayOrderRespVO preBuyPkg(Long pkgId) {
        PkgInfoDO pkgInfoDO = pkgInfoMapper.selectById(pkgId);
        if (ObjectUtils.isEmpty(pkgInfoDO) || !pkgInfoDO.getEnable()) {
            throw exception(DATA_NOT_EXISTS);
        }
        //检查最大购买数量限制
        if (pkgInfoDO.getMaxNum() > 0) {
            int c = pkgUserInfoMapper.countByUserId(pkgId, getLoginUserId());
            if (c >= pkgInfoDO.getMaxNum()) {
                throw exception(PKG_BUY_MAX_NUM_ERROR);
            }
        }
        //生成微信支付
        String orderNo = getOrderNo();
        WxPayOrderRespVO respVO = new WxPayOrderRespVO();
        respVO.setPrice(pkgInfoDO.getPrice().multiply(new BigDecimal("100")).intValue());
        respVO.setOrderNo(orderNo);
        //需要微信下单  先获取到该用户的openId
        String openId = socialUserApi.getUserOpenIdByType(getLoginUserId(), SocialTypeEnum.WECHAT_MINI_APP.getType());
        if (ObjectUtils.isEmpty(openId)) {
            throw exception(AUTH_USER_BIND_MINIAPP_ERROR);
        }
        //创建微信支付实例
        WxPayService wxPayService = myWxService.initWxPay(pkgInfoDO.getStoreId());
        try {
            //生成微信支付的订单
            WxPayMpOrderResult wxPayMpOrderResult = myWxService.createOrder(wxPayService, pkgInfoDO.getStoreId(), orderNo, respVO.getPayPrice(), openId);
//            wxPayUnifiedOrderRequest.setSignType("HMAC-SHA256");
//            wxPayUnifiedOrderRequest.setTimeExpire()
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
        payOrderService.create(getLoginUserId(), orderNo, null,pkgInfoDO.getStoreId(), AppEnum.order_pay_type.WEIXIN.getValue(), "套餐购买订单", respVO.getPrice());
        //把订单号存到redis 如果已经充值了 就移除这个订单号
        String redisKey = String.format(WX_PAY_ORDER, orderNo);
        Long tenantId = TenantContextHolder.getTenantId();
        // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
        if (tenantId == null) {
            LoginUser user = SecurityFrameworkUtils.getLoginUser();
            tenantId = user.getTenantId();
        }
        redisTemplate.opsForValue().set(redisKey, new WxPayOrderInfo(AppWxPayTypeEnum.PKG, orderNo, getLoginUserId(), tenantId
                , pkgInfoDO.getStoreId(), respVO.getPrice(), pkgId), 1, TimeUnit.DAYS);
        return respVO;
    }

    private String getOrderNo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDate = currentDateTime.format(dateFormatter);
        Random random = new Random();
        int randomNum = random.nextInt(100000000);
        String randomNumString = String.format("%08d", randomNum);
        return "TC" + currentDate + randomNumString;
    }

    @Override
    @Transactional
    public void buyPkg(AppBuyPkgReqVO reqVO) {
        if (ObjectUtils.isEmpty(reqVO.getUserId())) {
            reqVO.setUserId(getLoginUserId());
        }
        Long userId = reqVO.getUserId();
        PkgInfoDO pkgInfoDO = pkgInfoMapper.selectById(reqVO.getPkgId());
        if (ObjectUtils.isEmpty(pkgInfoDO) || !pkgInfoDO.getEnable()) {
            throw exception(DATA_NOT_EXISTS);
        }
        //检查最大购买数量限制
        if (pkgInfoDO.getMaxNum() > 0) {
            int c = pkgUserInfoMapper.countByUserId(reqVO.getPkgId(), userId);
            if (c >= pkgInfoDO.getMaxNum()) {
                throw exception(PKG_BUY_MAX_NUM_ERROR);
            }
        }
        payOrderService.checkWxOrder(reqVO.getOrderNo(), pkgInfoDO.getStoreId(), reqVO.getPrice());
        //添加套餐给用户
        PkgUserInfoDO pkgUserInfoDO = new PkgUserInfoDO();
        pkgUserInfoDO.setUserId(reqVO.getUserId());
        pkgUserInfoDO.setStatus(AppEnum.coupon_status.AVAILABLE.getValue());
        pkgUserInfoDO.setStoreId(pkgUserInfoDO.getStoreId());
        pkgUserInfoDO.setPkgId(pkgInfoDO.getPkgId());
        //计算过期时间
        if (pkgInfoDO.getExpireDay().compareTo(0) == 0) {
            //不过期
            pkgUserInfoDO.setExpireDate(LocalDate.of(2099, 12, 31));
        } else {
            LocalDate now = LocalDate.now();
            now = now.plusDays(pkgInfoDO.getExpireDay());
            pkgUserInfoDO.setExpireDate(now);
        }
        pkgUserInfoMapper.insert(pkgUserInfoDO);
    }

    @Override
    @Transactional
    public void delete(Long pkgId) {
        // 校验用户类型
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        PkgInfoDO pkgInfoDO = pkgInfoMapper.selectById(pkgId);
        if (ObjectUtils.isEmpty(pkgInfoDO)) {
            throw exception(OPRATION_ERROR);
        }
        pkgInfoMapper.deleteById(pkgId);

    }
}
