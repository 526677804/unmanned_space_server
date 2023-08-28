package com.yanzu.module.member.dal.mysql.discountrules;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.store.vo.AppDiscountRulesPageReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppDiscountRulesPageRespVO;
import com.yanzu.module.member.dal.dataobject.discountrules.DiscountRulesDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值优惠规则管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DiscountRulesMapper extends BaseMapperX<DiscountRulesDO> {


    List<AppDiscountRulesPageRespVO> getDiscountRulesPage(AppDiscountRulesPageReqVO reqVO);

    int changeDiscountRulesStatus(@Param("id") Long id, @Param("status") Integer status);

    int countByStoreIdAndPayMoney(@Param("storeId") Long storeId, @Param("payMoney") BigDecimal payMoney);

    List<String> getRulesByStoreId(Long storeId);

    List<AppDiscountRulesRespVO> getDiscountRulesByStoreId(Long storeId);
}
