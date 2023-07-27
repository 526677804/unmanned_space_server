package com.yanzu.module.member.convert.clearinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.clearinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;

/**
 * 保洁信息管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ClearInfoConvert {

    ClearInfoConvert INSTANCE = Mappers.getMapper(ClearInfoConvert.class);

    ClearInfoDO convert(ClearInfoCreateReqVO bean);

    ClearInfoDO convert(ClearInfoUpdateReqVO bean);

    ClearInfoRespVO convert(ClearInfoDO bean);

    List<ClearInfoRespVO> convertList(List<ClearInfoDO> list);

    PageResult<ClearInfoRespVO> convertPage(PageResult<ClearInfoDO> page);

    List<ClearInfoExcelVO> convertList02(List<ClearInfoDO> list);

}
