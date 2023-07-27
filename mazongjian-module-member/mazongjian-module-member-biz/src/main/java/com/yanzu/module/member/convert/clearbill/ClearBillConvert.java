package com.yanzu.module.member.convert.clearbill;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.clearbill.vo.*;
import com.yanzu.module.member.dal.dataobject.clearbill.ClearBillDO;

/**
 * 保洁账单管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface ClearBillConvert {

    ClearBillConvert INSTANCE = Mappers.getMapper(ClearBillConvert.class);

    ClearBillDO convert(ClearBillCreateReqVO bean);

    ClearBillDO convert(ClearBillUpdateReqVO bean);

    ClearBillRespVO convert(ClearBillDO bean);

    List<ClearBillRespVO> convertList(List<ClearBillDO> list);

    PageResult<ClearBillRespVO> convertPage(PageResult<ClearBillDO> page);

    List<ClearBillExcelVO> convertList02(List<ClearBillDO> list);

}
