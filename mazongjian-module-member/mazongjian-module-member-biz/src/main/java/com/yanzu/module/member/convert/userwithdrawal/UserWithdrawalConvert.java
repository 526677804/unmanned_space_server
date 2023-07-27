package com.yanzu.module.member.convert.userwithdrawal;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.userwithdrawal.vo.*;
import com.yanzu.module.member.dal.dataobject.userwithdrawal.UserWithdrawalDO;

/**
 * 用户提现 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface UserWithdrawalConvert {

    UserWithdrawalConvert INSTANCE = Mappers.getMapper(UserWithdrawalConvert.class);

    UserWithdrawalDO convert(UserWithdrawalCreateReqVO bean);

    UserWithdrawalDO convert(UserWithdrawalUpdateReqVO bean);

    UserWithdrawalRespVO convert(UserWithdrawalDO bean);

    List<UserWithdrawalRespVO> convertList(List<UserWithdrawalDO> list);

    PageResult<UserWithdrawalRespVO> convertPage(PageResult<UserWithdrawalDO> page);

    List<UserWithdrawalExcelVO> convertList02(List<UserWithdrawalDO> list);

}
