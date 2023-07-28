package com.yanzu.module.member.convert.franchiseinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.controller.app.user.vo.AppFranchiseInfoReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;

/**
 * 加盟信息 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface FranchiseInfoConvert {

    FranchiseInfoConvert INSTANCE = Mappers.getMapper(FranchiseInfoConvert.class);

    FranchiseInfoDO convert2(AppFranchiseInfoReqVO reqVO);
}
