package com.yanzu.module.member.service.userwithdrawal;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.userwithdrawal.vo.*;
import com.yanzu.module.member.dal.dataobject.userwithdrawal.UserWithdrawalDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.userwithdrawal.UserWithdrawalConvert;
import com.yanzu.module.member.dal.mysql.userwithdrawal.UserWithdrawalMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 用户提现 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class UserWithdrawalServiceImpl implements UserWithdrawalService {

    @Resource
    private UserWithdrawalMapper userWithdrawalMapper;



    @Override
    public UserWithdrawalDO getUserWithdrawal(Long id) {
        return userWithdrawalMapper.selectById(id);
    }

    @Override
    public List<UserWithdrawalDO> getUserWithdrawalList(Collection<Long> ids) {
        return userWithdrawalMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<UserWithdrawalDO> getUserWithdrawalPage(UserWithdrawalPageReqVO pageReqVO) {
        return userWithdrawalMapper.selectPage(pageReqVO);
    }

    @Override
    public List<UserWithdrawalDO> getUserWithdrawalList(UserWithdrawalExportReqVO exportReqVO) {
        return userWithdrawalMapper.selectList(exportReqVO);
    }

}
