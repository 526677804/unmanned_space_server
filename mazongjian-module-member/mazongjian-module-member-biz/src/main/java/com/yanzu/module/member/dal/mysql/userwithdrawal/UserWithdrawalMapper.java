package com.yanzu.module.member.dal.mysql.userwithdrawal;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.manager.vo.AppWithdrawalPageReqVO;
import com.yanzu.module.member.controller.app.manager.vo.AppWithdrawalPageRespVO;
import com.yanzu.module.member.dal.dataobject.userwithdrawal.UserWithdrawalDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户提现 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface UserWithdrawalMapper extends BaseMapperX<UserWithdrawalDO> {
    List<AppWithdrawalPageRespVO> getWithdrawalPage(AppWithdrawalPageReqVO reqVO);

}
