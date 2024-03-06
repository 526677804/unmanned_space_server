package com.yanzu.module.member.convert.franchiseinfo;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.FranchiseInfoCreateReqVO;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.FranchiseInfoExcelVO;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.FranchiseInfoRespVO;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.FranchiseInfoUpdateReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppFranchiseInfoReqVO;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 加盟信息 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface FranchiseInfoConvert {

    FranchiseInfoConvert INSTANCE = Mappers.getMapper(FranchiseInfoConvert.class);

    FranchiseInfoDO convert2(AppFranchiseInfoReqVO reqVO);

    FranchiseInfoDO convert(FranchiseInfoCreateReqVO bean);

    FranchiseInfoDO convert(FranchiseInfoUpdateReqVO bean);

    FranchiseInfoRespVO convert(FranchiseInfoDO bean);

    List<FranchiseInfoRespVO> convertList(List<FranchiseInfoDO> list);

    PageResult<FranchiseInfoRespVO> convertPage(PageResult<FranchiseInfoDO> page);

    List<FranchiseInfoExcelVO> convertList02(List<FranchiseInfoDO> list);
}
