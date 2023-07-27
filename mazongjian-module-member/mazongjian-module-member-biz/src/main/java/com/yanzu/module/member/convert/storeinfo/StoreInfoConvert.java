package com.yanzu.module.member.convert.storeinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.storeinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;

/**
 * 门店管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface StoreInfoConvert {

    StoreInfoConvert INSTANCE = Mappers.getMapper(StoreInfoConvert.class);

    StoreInfoDO convert(StoreInfoCreateReqVO bean);

    StoreInfoDO convert(StoreInfoUpdateReqVO bean);

    StoreInfoRespVO convert(StoreInfoDO bean);

    List<StoreInfoRespVO> convertList(List<StoreInfoDO> list);

    PageResult<StoreInfoRespVO> convertPage(PageResult<StoreInfoDO> page);

    List<StoreInfoExcelVO> convertList02(List<StoreInfoDO> list);

}
