package com.yanzu.module.member.convert.member;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.groupPay.vo.*;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;

/**
 * 团购支付信息 Convert
 *
 * @author MrGuan
 */
@Mapper
public interface GroupPayInfoConvert {

    GroupPayInfoConvert INSTANCE = Mappers.getMapper(GroupPayInfoConvert.class);

    GroupPayInfoDO convert(GroupPayInfoCreateReqVO bean);

    GroupPayInfoDO convert(GroupPayInfoUpdateReqVO bean);

    GroupPayInfoRespVO convert(GroupPayInfoDO bean);

    List<GroupPayInfoRespVO> convertList(List<GroupPayInfoDO> list);

    PageResult<GroupPayInfoRespVO> convertPage(PageResult<GroupPayInfoDO> page);

    List<GroupPayInfoExcelVO> convertList02(List<GroupPayInfoDO> list);

}
