package com.yanzu.module.member.convert.storeproduct;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.storeproduct.vo.*;
import com.yanzu.module.member.dal.dataobject.storeproduct.StoreProductDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 商品 Convert
 *
 * @author yshop
 */
@Mapper
public interface StoreProductConvert {

    StoreProductConvert INSTANCE = Mappers.getMapper(StoreProductConvert.class);

    StoreProductDO convert(StoreProductCreateReqVO bean);

    StoreProductDO convert(StoreProductUpdateReqVO bean);

    StoreProductRespVO convert(StoreProductDO bean);

    AppStoreProductRespVo convert01(StoreProductDO bean);

    List<StoreProductRespVO> convertList(List<StoreProductDO> list);

    PageResult<StoreProductRespVO> convertPage(PageResult<StoreProductDO> page);

    List<StoreProductExcelVO> convertList02(List<StoreProductDO> list);

    List<AppStoreProductRespVo> convertList03(List<StoreProductDO> list);
}
