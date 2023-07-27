package com.yanzu.module.member.convert.discountrules;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.discountrules.vo.*;
import com.yanzu.module.member.dal.dataobject.discountrules.DiscountRulesDO;

/**
 * 充值优惠规则管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface DiscountRulesConvert {

    DiscountRulesConvert INSTANCE = Mappers.getMapper(DiscountRulesConvert.class);

    DiscountRulesDO convert(DiscountRulesCreateReqVO bean);

    DiscountRulesDO convert(DiscountRulesUpdateReqVO bean);

    DiscountRulesRespVO convert(DiscountRulesDO bean);

    List<DiscountRulesRespVO> convertList(List<DiscountRulesDO> list);

    PageResult<DiscountRulesRespVO> convertPage(PageResult<DiscountRulesDO> page);

    List<DiscountRulesExcelVO> convertList02(List<DiscountRulesDO> list);

}
