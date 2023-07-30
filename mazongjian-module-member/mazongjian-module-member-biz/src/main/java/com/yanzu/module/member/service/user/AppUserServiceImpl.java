package com.yanzu.module.member.service.user;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.enums.CommonStatusEnum;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.infra.api.file.FileApi;
import com.yanzu.module.member.controller.app.user.vo.*;
import com.yanzu.module.member.convert.franchiseinfo.FranchiseInfoConvert;
import com.yanzu.module.member.convert.user.UserConvert;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.couponinfo.CouponInfoMapper;
import com.yanzu.module.member.dal.mysql.franchiseinfo.FranchiseInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.system.api.sms.SmsCodeApi;
import com.yanzu.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import com.yanzu.module.system.api.tenant.TenantApi;
import com.yanzu.module.system.enums.sms.SmsSceneEnum;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getTenantId;
import static com.yanzu.module.member.enums.ErrorCodeConstants.USER_NOT_EXISTS;

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
        memberUserMapper.updateById(new MemberUserDO().setId(id)
                .setLoginIp(loginIp).setLoginDate(LocalDateTime.now()));
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
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(reqVO.getOldMobile()).setCode(reqVO.getOldCode())
                .setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));
        // 使用新验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(reqVO.getMobile()).setCode(reqVO.getCode())
                .setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));

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
        //查询赠送余额
        respVO.setGiftBalance(storeUserMapper.getGiftBalanceByUserId(loginUserId));
        //查询可用优惠券数量
        respVO.setCouponCount(couponInfoMapper.countByUserId(loginUserId));
        return respVO;
    }

    @Override
    public PageResult<AppUserMoneyBillRespVO> getOrderPage(AppUserMoneyBillPageReqVO reqVO) {
        PageHelper.startPage(reqVO);
        List<AppUserMoneyBillRespVO> list = userMoneyBillMapper.getOrderPage(reqVO);
        PageInfo<AppUserMoneyBillRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
//        // 构建分页对象
//        IPage<AppUserMoneyBillRespVO> page = new Page<>(reqVO.getPageNo(),reqVO.getPageSize());
//        // 调用查询方法
//        IPage<AppUserMoneyBillRespVO> roadSectionIPage = userMoneyBillMapper.selectPage(page, reqVO);
//        // 从分页对象中取出查询结果
//        List<RoadSectionVO> records = roadSectionIPage.getRecords();

//        return null;
    }

    @Override
    public List<AppGiftBalanceListRespVO> getGiftBalanceList() {
        return storeUserMapper.getGiftBalanceList(getLoginUserId());
    }

    @Override
    @Transactional
    public void eechargeBalance(AppRechargeBalanceReqVO reqVO) {
        if (ObjectUtils.isEmpty(reqVO.getUserId())) {
            reqVO.setUserId(getLoginUserId());
        }

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
        PageHelper.startPage(reqVO);
        List<AppCouponPageRespVO> list = couponInfoMapper.getCouponPage(reqVO);
        PageInfo<AppCouponPageRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
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
