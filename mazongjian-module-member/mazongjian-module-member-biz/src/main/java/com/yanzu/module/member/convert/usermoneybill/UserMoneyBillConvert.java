package com.yanzu.module.member.convert.usermoneybill;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.usermoneybill.vo.*;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;

/**
 * 用户账单明细 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface UserMoneyBillConvert {

    UserMoneyBillConvert INSTANCE = Mappers.getMapper(UserMoneyBillConvert.class);

    UserMoneyBillDO convert(UserMoneyBillCreateReqVO bean);

    UserMoneyBillDO convert(UserMoneyBillUpdateReqVO bean);

    UserMoneyBillRespVO convert(UserMoneyBillDO bean);

    List<UserMoneyBillRespVO> convertList(List<UserMoneyBillDO> list);

    PageResult<UserMoneyBillRespVO> convertPage(PageResult<UserMoneyBillDO> page);

    List<UserMoneyBillExcelVO> convertList02(List<UserMoneyBillDO> list);

}
