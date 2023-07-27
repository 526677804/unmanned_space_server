package com.yanzu.module.member.service.userwithdrawal;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.userwithdrawal.vo.*;
import com.yanzu.module.member.dal.dataobject.userwithdrawal.UserWithdrawalDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 用户提现 Service 接口
 *
 * @author 芋道源码
 */
public interface UserWithdrawalService {


    /**
     * 获得用户提现
     *
     * @param id 编号
     * @return 用户提现
     */
    UserWithdrawalDO getUserWithdrawal(Long id);

    /**
     * 获得用户提现列表
     *
     * @param ids 编号
     * @return 用户提现列表
     */
    List<UserWithdrawalDO> getUserWithdrawalList(Collection<Long> ids);

    /**
     * 获得用户提现分页
     *
     * @param pageReqVO 分页查询
     * @return 用户提现分页
     */
    PageResult<UserWithdrawalDO> getUserWithdrawalPage(UserWithdrawalPageReqVO pageReqVO);

    /**
     * 获得用户提现列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 用户提现列表
     */
    List<UserWithdrawalDO> getUserWithdrawalList(UserWithdrawalExportReqVO exportReqVO);

}
