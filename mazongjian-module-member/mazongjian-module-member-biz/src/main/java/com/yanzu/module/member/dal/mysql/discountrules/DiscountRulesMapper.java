package com.yanzu.module.member.dal.mysql.discountrules;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.discountrules.DiscountRulesDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.discountrules.vo.*;

/**
 * 充值优惠规则管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DiscountRulesMapper extends BaseMapperX<DiscountRulesDO> {

    default PageResult<DiscountRulesDO> selectPage(DiscountRulesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DiscountRulesDO>()
//                .betweenIfPresent(DiscountRulesDO::getPayMoney, reqVO.getPayMoney())
//                .betweenIfPresent(DiscountRulesDO::getGiftMoney, reqVO.getGiftMoney())
                .betweenIfPresent(DiscountRulesDO::getExpriceTime, reqVO.getExpriceTime())
                .eqIfPresent(DiscountRulesDO::getStoreIds, reqVO.getStoreIds())
                .eqIfPresent(DiscountRulesDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DiscountRulesDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(DiscountRulesDO::getId)
        );
    }

    default List<DiscountRulesDO> selectList(DiscountRulesExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DiscountRulesDO>()
//                .betweenIfPresent(DiscountRulesDO::getPayMoney, reqVO.getPayMoney())
//                .betweenIfPresent(DiscountRulesDO::getGiftMoney, reqVO.getGiftMoney())
                .betweenIfPresent(DiscountRulesDO::getExpriceTime, reqVO.getExpriceTime())
                .eqIfPresent(DiscountRulesDO::getStoreIds, reqVO.getStoreIds())
                .eqIfPresent(DiscountRulesDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DiscountRulesDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(DiscountRulesDO::getId)
        );
    }

}
