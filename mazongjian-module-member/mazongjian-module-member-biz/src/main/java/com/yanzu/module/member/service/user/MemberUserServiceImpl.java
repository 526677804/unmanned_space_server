package com.yanzu.module.member.service.user;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.user.vo.AppUserExportReqVO;
import com.yanzu.module.member.controller.admin.user.vo.AppUserPageReqVO;
import com.yanzu.module.member.controller.admin.user.vo.AppUserUpdateReqVO;
import com.yanzu.module.member.convert.user.AppUserConvert;
import com.yanzu.module.member.dal.dataobject.user.AppUserDO;
import com.yanzu.module.member.dal.mysql.user.AppUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DATA_NOT_EXISTS;
import static com.yanzu.module.member.enums.ErrorCodeConstants.OPRATION_ERROR;

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

}
