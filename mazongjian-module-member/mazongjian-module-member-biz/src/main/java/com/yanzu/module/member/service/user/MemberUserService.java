package com.yanzu.module.member.service.user;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.user.vo.*;
import com.yanzu.module.member.dal.dataobject.user.AppUserDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 用户管理 Service 接口
 *
 * @author 芋道源码
 */
public interface MemberUserService {



    /**
     * 更新用户管理
     *
     * @param updateReqVO 更新信息
     */
    void updateAppUser(@Valid AppUserUpdateReqVO updateReqVO);

    /**
     * 删除用户管理
     *
     * @param id 编号
     */
    void deleteAppUser(Long id);

    /**
     * 获得用户管理
     *
     * @param id 编号
     * @return 用户管理
     */
    AppUserDO getAppUser(Long id);

    /**
     * 获得用户管理列表
     *
     * @param ids 编号
     * @return 用户管理列表
     */
    List<AppUserDO> getAppUserList(Collection<Long> ids);

    /**
     * 获得用户管理分页
     *
     * @param pageReqVO 分页查询
     * @return 用户管理分页
     */
    PageResult<AppUserDO> getAppUserPage(AppUserPageReqVO pageReqVO);

    /**
     * 获得用户管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 用户管理列表
     */
    List<AppUserDO> getAppUserList(AppUserExportReqVO exportReqVO);

    Long createAppUser(AppUserCreateReqVO createReqVO);

    void recharge(AppUserRechargeReqVO reqVO);

}
