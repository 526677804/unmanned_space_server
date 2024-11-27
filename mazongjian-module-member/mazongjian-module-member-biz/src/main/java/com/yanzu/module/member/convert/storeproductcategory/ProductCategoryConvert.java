package com.yanzu.module.member.convert.storeproductcategory;

import com.yanzu.module.member.controller.app.storeproductcategory.vo.AppCategoryRespVO;
import com.yanzu.module.member.controller.app.storeproductcategory.vo.ProductCategoryCreateReqVO;
import com.yanzu.module.member.controller.app.storeproductcategory.vo.ProductCategoryRespVO;
import com.yanzu.module.member.controller.app.storeproductcategory.vo.ProductCategoryUpdateReqVO;
import com.yanzu.module.member.dal.dataobject.storeproductcategory.ProductCategoryDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 商品分类 Convert
 *
 * @author yshop
 */
@Mapper
public interface ProductCategoryConvert {

    ProductCategoryConvert INSTANCE = Mappers.getMapper(ProductCategoryConvert.class);

    ProductCategoryDO convert(ProductCategoryCreateReqVO bean);

    ProductCategoryDO convert(ProductCategoryUpdateReqVO bean);

    ProductCategoryRespVO convert(ProductCategoryDO bean);

    List<ProductCategoryRespVO> convertList(List<ProductCategoryDO> list);

    List<AppCategoryRespVO> convertList03(List<ProductCategoryDO> list);
}
