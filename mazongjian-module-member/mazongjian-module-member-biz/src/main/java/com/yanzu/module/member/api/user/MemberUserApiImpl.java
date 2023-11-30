package com.yanzu.module.member.api.user;

import com.yanzu.module.member.api.user.dto.MemberUserRespDTO;
import com.yanzu.module.member.convert.user.UserConvert;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.couponinfo.CouponInfoMapper;
import com.yanzu.module.member.service.order.AppOrderService;
import com.yanzu.module.member.service.user.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 会员用户的 API 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class MemberUserApiImpl implements MemberUserApi {

    @Resource
    private AppUserService userService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private CouponInfoMapper couponInfoMapper;


    @Override
    public MemberUserRespDTO getUser(Long id) {
        MemberUserDO user = userService.getUser(id);
        return UserConvert.INSTANCE.convert2(user);
    }

    @Override
    public List<MemberUserRespDTO> getUsers(Collection<Long> ids) {
        return UserConvert.INSTANCE.convertList2(userService.getUserList(ids));
    }

    @Override
    public List<MemberUserRespDTO> getUserListByNickname(String nickname) {
        return UserConvert.INSTANCE.convertList2(userService.getUserListByNickname(nickname));
    }

    @Override
    public MemberUserRespDTO getUserByMobile(String mobile) {
        return UserConvert.INSTANCE.convert2(userService.getUserByMobile(mobile));
    }

    /**
     * 订单处理的定时任务，每分钟执行一次， 用于到时间开始订单 或者 结束订单
     */
    @Override
    public void executeOrderJob() {
        appOrderService.executeOrderJob();

    }

    @Override
    public void executeMeituanRefreshTokenJob() {
        appOrderService.executeMeituanRefreshTokenJob();
    }

    @Override
    @Transactional
    public void executeCouponExpire() {
        log.info("==========     开始执行优惠券定时检查任务     ==========");
        //处理过期 但是未使用的优惠券
        couponInfoMapper.executeCouponExpire();
    }

}
