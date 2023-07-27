package com.yanzu.module.member.service.discountrules;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.discountrules.vo.*;
import com.yanzu.module.member.dal.dataobject.discountrules.DiscountRulesDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.discountrules.DiscountRulesConvert;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 充值优惠规则管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DiscountRulesServiceImpl implements DiscountRulesService {

    @Resource
    private DiscountRulesMapper discountRulesMapper;

    @Override
    public Long createDiscountRules(DiscountRulesCreateReqVO createReqVO) {
        // 插入
        DiscountRulesDO discountRules = DiscountRulesConvert.INSTANCE.convert(createReqVO);
        discountRulesMapper.insert(discountRules);
        // 返回
        return discountRules.getDiscountId();
    }

    @Override
    public void updateDiscountRules(DiscountRulesUpdateReqVO updateReqVO) {
        // 校验存在
        validateDiscountRulesExists(updateReqVO.getDiscountId());
        // 更新
        DiscountRulesDO updateObj = DiscountRulesConvert.INSTANCE.convert(updateReqVO);
        discountRulesMapper.updateById(updateObj);
    }

    @Override
    public void deleteDiscountRules(Long id) {
        // 校验存在
        validateDiscountRulesExists(id);
        // 删除
        discountRulesMapper.deleteById(id);
    }

    private void validateDiscountRulesExists(Long id) {
        if (discountRulesMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public DiscountRulesDO getDiscountRules(Long id) {
        return discountRulesMapper.selectById(id);
    }

    @Override
    public List<DiscountRulesDO> getDiscountRulesList(Collection<Long> ids) {
        return discountRulesMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DiscountRulesDO> getDiscountRulesPage(DiscountRulesPageReqVO pageReqVO) {
        return discountRulesMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DiscountRulesDO> getDiscountRulesList(DiscountRulesExportReqVO exportReqVO) {
        return discountRulesMapper.selectList(exportReqVO);
    }

}
