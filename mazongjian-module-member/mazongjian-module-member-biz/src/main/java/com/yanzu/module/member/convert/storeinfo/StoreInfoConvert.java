package com.yanzu.module.member.convert.storeinfo;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoCreateReqVO;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoExcelVO;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoRespVO;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoUpdateReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreInfoReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreInfoRespVO;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    AppStoreInfoRespVO convert2(StoreInfoDO storeInfoDO);

    StoreInfoDO convert3(AppStoreInfoReqVO reqVO);
}
