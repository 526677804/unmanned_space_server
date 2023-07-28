package com.yanzu.module.member.convert.storeinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.controller.app.store.vo.AppStoreInfoReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreInfoRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;

/**
 * 门店管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface StoreInfoConvert {

    StoreInfoConvert INSTANCE = Mappers.getMapper(StoreInfoConvert.class);


    StoreInfoDO convert(AppStoreInfoReqVO bean);


    AppStoreInfoRespVO convert2(StoreInfoDO storeInfoDO);
}
