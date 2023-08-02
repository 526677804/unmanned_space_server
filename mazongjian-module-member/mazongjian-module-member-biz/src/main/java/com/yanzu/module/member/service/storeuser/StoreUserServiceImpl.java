package com.yanzu.module.member.service.storeuser;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.storeuser.vo.StoreUserCreateReqVO;
import com.yanzu.module.member.controller.admin.storeuser.vo.StoreUserExportReqVO;
import com.yanzu.module.member.controller.admin.storeuser.vo.StoreUserPageReqVO;
import com.yanzu.module.member.controller.admin.storeuser.vo.StoreUserUpdateReqVO;
import com.yanzu.module.member.convert.storeuser.StoreUserConvert;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DATA_NOT_EXISTS;

/**
 * 门店用户管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class StoreUserServiceImpl implements StoreUserService {

    @Resource
    private StoreUserMapper storeUserMapper;

    @Override
    public Long createStoreUser(StoreUserCreateReqVO createReqVO) {
        // 插入
        StoreUserDO storeUser = StoreUserConvert.INSTANCE.convert(createReqVO);
        storeUserMapper.insert(storeUser);
        // 返回
        return storeUser.getId();
    }

    @Override
    public void updateStoreUser(StoreUserUpdateReqVO updateReqVO) {
        // 校验存在
        validateStoreUserExists(updateReqVO.getId());
        // 更新
        StoreUserDO updateObj = StoreUserConvert.INSTANCE.convert(updateReqVO);
        storeUserMapper.updateById(updateObj);
    }

    @Override
    public void deleteStoreUser(Long id) {
        // 校验存在
        validateStoreUserExists(id);
        // 删除
        storeUserMapper.deleteById(id);
    }

    private void validateStoreUserExists(Long id) {
        if (storeUserMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public StoreUserDO getStoreUser(Long id) {
        return storeUserMapper.selectById(id);
    }

    @Override
    public List<StoreUserDO> getStoreUserList(Collection<Long> ids) {
        return storeUserMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<StoreUserDO> getStoreUserPage(StoreUserPageReqVO pageReqVO) {
        return storeUserMapper.selectPage(pageReqVO);
    }

    @Override
    public List<StoreUserDO> getStoreUserList(StoreUserExportReqVO exportReqVO) {
        return storeUserMapper.selectList(exportReqVO);
    }

}
