package com.yanzu.module.member.service.storeuser;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.storeuser.vo.*;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 门店用户管理 Service 接口
 *
 * @author 芋道源码
 */
public interface StoreUserService {

    /**
     * 创建门店用户管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStoreUser(@Valid StoreUserCreateReqVO createReqVO);

    /**
     * 更新门店用户管理
     *
     * @param updateReqVO 更新信息
     */
    void updateStoreUser(@Valid StoreUserUpdateReqVO updateReqVO);

    /**
     * 删除门店用户管理
     *
     * @param id 编号
     */
    void deleteStoreUser(Long id);

    /**
     * 获得门店用户管理
     *
     * @param id 编号
     * @return 门店用户管理
     */
    StoreUserDO getStoreUser(Long id);

    /**
     * 获得门店用户管理列表
     *
     * @param ids 编号
     * @return 门店用户管理列表
     */
    List<StoreUserDO> getStoreUserList(Collection<Long> ids);

    /**
     * 获得门店用户管理分页
     *
     * @param pageReqVO 分页查询
     * @return 门店用户管理分页
     */
    PageResult<StoreUserDO> getStoreUserPage(StoreUserPageReqVO pageReqVO);

    /**
     * 获得门店用户管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 门店用户管理列表
     */
    List<StoreUserDO> getStoreUserList(StoreUserExportReqVO exportReqVO);

}
