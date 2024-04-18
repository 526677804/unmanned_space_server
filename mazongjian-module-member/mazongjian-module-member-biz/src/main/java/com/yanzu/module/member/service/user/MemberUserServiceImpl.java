package com.yanzu.module.member.service.user;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.user.vo.AppUserCreateReqVO;
import com.yanzu.module.member.controller.admin.user.vo.AppUserExportReqVO;
import com.yanzu.module.member.controller.admin.user.vo.AppUserPageReqVO;
import com.yanzu.module.member.controller.admin.user.vo.AppUserUpdateReqVO;
import com.yanzu.module.member.convert.user.AppUserConvert;
import com.yanzu.module.member.dal.dataobject.user.AppUserDO;
import com.yanzu.module.member.dal.mysql.user.AppUserMapper;
import com.yanzu.module.system.api.oauth2.OAuth2TokenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 用户管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class MemberUserServiceImpl implements MemberUserService {

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private OAuth2TokenApi oAuth2TokenApi;

    @Override
    public void updateAppUser(AppUserUpdateReqVO updateReqVO) {
        // 校验存在
        validateAppUserExists(updateReqVO.getId());
        if (updateReqVO.getUserType().byteValue() == 2) {
            throw exception(OPRATION_ERROR);
        }
        // 更新
        AppUserDO updateObj = AppUserConvert.INSTANCE.convert(updateReqVO);
        appUserMapper.updateById(updateObj);
        //如果修改了用户类型，需要用户重新登录  否则代码中有很多从token中获取用户类型的地方要出错
        oAuth2TokenApi.removeAccessTokenByUserId(updateObj.getId());

    }

    @Override
    public void deleteAppUser(Long id) {
        // 校验存在
        validateAppUserExists(id);
        // 删除
        appUserMapper.deleteById(id);
    }

    private void validateAppUserExists(Long id) {
        if (appUserMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public AppUserDO getAppUser(Long id) {
        return appUserMapper.selectById(id);
    }

    @Override
    public List<AppUserDO> getAppUserList(Collection<Long> ids) {
        return appUserMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<AppUserDO> getAppUserPage(AppUserPageReqVO pageReqVO) {
        return appUserMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AppUserDO> getAppUserList(AppUserExportReqVO exportReqVO) {
        return appUserMapper.selectList(exportReqVO);
    }

    @Override
    @Transactional
    public Long createAppUser(AppUserCreateReqVO createReqVO) {
        //手机号不能重复
        Long count = appUserMapper.selectCount("mobile", createReqVO.getMobile());
        if (count.intValue() > 0) {
            throw exception(USER_EXISTS);
        }
        AppUserDO user = new AppUserDO();
        user.setNickname("用户" + createReqVO.getMobile().substring(5, 11));
        user.setMobile(createReqVO.getMobile());
        user.setStatus(createReqVO.getStatus());
        user.setUserType(createReqVO.getUserType());
        user.setRegisterIp("127.0.0.1");
        appUserMapper.insert(user);
        return user.getId();
    }

}
