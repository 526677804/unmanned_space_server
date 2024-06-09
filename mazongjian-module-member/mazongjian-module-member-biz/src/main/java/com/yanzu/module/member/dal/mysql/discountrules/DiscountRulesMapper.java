package com.yanzu.module.member.dal.mysql.discountrules;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.order.vo.AppDiscountRulesRespVO;
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


    IPage<AppDiscountRulesPageRespVO> getDiscountRulesPage(@Param("page") IPage<AppDiscountRulesPageRespVO> page, @Param("reqVO") AppDiscountRulesPageReqVO reqVO);

    int changeDiscountRulesStatus(@Param("id") Long id, @Param("status") Integer status);

    int countByStoreIdAndPayMoney(@Param("storeId") Long storeId, @Param("payMoney") BigDecimal payMoney, @Param("discountId") Long discountId);

    List<String> getRulesByStoreId(Long storeId);

    List<AppDiscountRulesRespVO> getDiscountRulesByStoreId(Long storeId);

    BigDecimal getMaxGiftByStoreIdAndPrice(@Param("storeId") Long storeId, @Param("payMoney") BigDecimal payMoney);

    int executeExpire();
}
