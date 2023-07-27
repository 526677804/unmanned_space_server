package com.yanzu.module.member.service.discountrules;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.discountrules.vo.*;
import com.yanzu.module.member.dal.dataobject.discountrules.DiscountRulesDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 充值优惠规则管理 Service 接口
 *
 * @author 芋道源码
 */
public interface DiscountRulesService {

    /**
     * 创建充值优惠规则管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDiscountRules(@Valid DiscountRulesCreateReqVO createReqVO);

    /**
     * 更新充值优惠规则管理
     *
     * @param updateReqVO 更新信息
     */
    void updateDiscountRules(@Valid DiscountRulesUpdateReqVO updateReqVO);

    /**
     * 删除充值优惠规则管理
     *
     * @param id 编号
     */
    void deleteDiscountRules(Long id);

    /**
     * 获得充值优惠规则管理
     *
     * @param id 编号
     * @return 充值优惠规则管理
     */
    DiscountRulesDO getDiscountRules(Long id);

    /**
     * 获得充值优惠规则管理列表
     *
     * @param ids 编号
     * @return 充值优惠规则管理列表
     */
    List<DiscountRulesDO> getDiscountRulesList(Collection<Long> ids);

    /**
     * 获得充值优惠规则管理分页
     *
     * @param pageReqVO 分页查询
     * @return 充值优惠规则管理分页
     */
    PageResult<DiscountRulesDO> getDiscountRulesPage(DiscountRulesPageReqVO pageReqVO);

    /**
     * 获得充值优惠规则管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 充值优惠规则管理列表
     */
    List<DiscountRulesDO> getDiscountRulesList(DiscountRulesExportReqVO exportReqVO);

}
