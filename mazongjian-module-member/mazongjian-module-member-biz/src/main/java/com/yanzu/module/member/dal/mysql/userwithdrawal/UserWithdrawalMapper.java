package com.yanzu.module.member.dal.mysql.userwithdrawal;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.userwithdrawal.UserWithdrawalDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.userwithdrawal.vo.*;

/**
 * 用户提现 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface UserWithdrawalMapper extends BaseMapperX<UserWithdrawalDO> {

    default PageResult<UserWithdrawalDO> selectPage(UserWithdrawalPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserWithdrawalDO>()
                .likeIfPresent(UserWithdrawalDO::getNo, reqVO.getNo())
                .eqIfPresent(UserWithdrawalDO::getUserId, reqVO.getUserId())
//                .betweenIfPresent(UserWithdrawalDO::getMoney, reqVO.getMoney())
                .betweenIfPresent(UserWithdrawalDO::getFinishTime, reqVO.getFinishTime())
                .eqIfPresent(UserWithdrawalDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(UserWithdrawalDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserWithdrawalDO::getId));
    }

    default List<UserWithdrawalDO> selectList(UserWithdrawalExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<UserWithdrawalDO>()
                .likeIfPresent(UserWithdrawalDO::getNo, reqVO.getNo())
                .eqIfPresent(UserWithdrawalDO::getUserId, reqVO.getUserId())
//                .betweenIfPresent(UserWithdrawalDO::getMoney, reqVO.getMoney())
                .betweenIfPresent(UserWithdrawalDO::getFinishTime, reqVO.getFinishTime())
                .eqIfPresent(UserWithdrawalDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(UserWithdrawalDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserWithdrawalDO::getId));
    }

}
