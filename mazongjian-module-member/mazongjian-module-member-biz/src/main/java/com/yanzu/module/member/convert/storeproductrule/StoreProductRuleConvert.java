package com.yanzu.module.member.convert.storeproductrule;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.storeproductrule.vo.StoreProductRuleCreateReqVO;
import com.yanzu.module.member.controller.app.storeproductrule.vo.StoreProductRuleExcelVO;
import com.yanzu.module.member.controller.app.storeproductrule.vo.StoreProductRuleRespVO;
import com.yanzu.module.member.controller.app.storeproductrule.vo.StoreProductRuleUpdateReqVO;
import com.yanzu.module.member.dal.dataobject.storeproductrule.StoreProductRuleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 商品规则值(规格) Convert
 *
 * @author yshop
 */
@Mapper
public interface StoreProductRuleConvert {

    StoreProductRuleConvert INSTANCE = Mappers.getMapper(StoreProductRuleConvert.class);

    StoreProductRuleDO convert(StoreProductRuleCreateReqVO bean);

    StoreProductRuleDO convert(StoreProductRuleUpdateReqVO bean);

    StoreProductRuleRespVO convert(StoreProductRuleDO bean);

    List<StoreProductRuleRespVO> convertList(List<StoreProductRuleDO> list);

    PageResult<StoreProductRuleRespVO> convertPage(PageResult<StoreProductRuleDO> page);

    List<StoreProductRuleExcelVO> convertList02(List<StoreProductRuleDO> list);

}
