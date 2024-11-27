package com.yanzu.module.member.service.storeproductrule;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.storeproductrule.vo.StoreProductRuleCreateReqVO;
import com.yanzu.module.member.controller.app.storeproductrule.vo.StoreProductRuleExportReqVO;
import com.yanzu.module.member.controller.app.storeproductrule.vo.StoreProductRulePageReqVO;
import com.yanzu.module.member.controller.app.storeproductrule.vo.StoreProductRuleUpdateReqVO;
import com.yanzu.module.member.convert.storeproductrule.StoreProductRuleConvert;
import com.yanzu.module.member.dal.dataobject.storeproductrule.StoreProductRuleDO;
import com.yanzu.module.member.dal.mysql.storeproductrule.StoreProductRuleMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.system.enums.ErrorCodeConstants.STORE_PRODUCT_RULE_NOT_EXISTS;


/**
 * 商品规则值(规格) Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StoreProductRuleServiceImpl implements StoreProductRuleService {

    @Resource
    private StoreProductRuleMapper storeProductRuleMapper;

    @Override
    public Integer createStoreProductRule(StoreProductRuleCreateReqVO createReqVO) {
        // 插入
        StoreProductRuleDO storeProductRule = StoreProductRuleConvert.INSTANCE.convert(createReqVO);
        storeProductRuleMapper.insert(storeProductRule);
        // 返回
        return storeProductRule.getId();
    }

    @Override
    public void updateStoreProductRule(StoreProductRuleUpdateReqVO updateReqVO) {
        // 校验存在
        validateStoreProductRuleExists(updateReqVO.getId());
        // 更新
        StoreProductRuleDO updateObj = StoreProductRuleConvert.INSTANCE.convert(updateReqVO);
        storeProductRuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteStoreProductRule(Integer id) {
        // 校验存在
        validateStoreProductRuleExists(id);
        // 删除
        storeProductRuleMapper.deleteById(id);
    }

    private void validateStoreProductRuleExists(Integer id) {
        if (storeProductRuleMapper.selectById(id) == null) {
            throw exception(STORE_PRODUCT_RULE_NOT_EXISTS);
        }
    }

    @Override
    public StoreProductRuleDO getStoreProductRule(Integer id) {
        return storeProductRuleMapper.selectById(id);
    }

    @Override
    public List<StoreProductRuleDO> getStoreProductRuleList(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return storeProductRuleMapper.selectList();
        }
        return storeProductRuleMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<StoreProductRuleDO> getStoreProductRulePage(StoreProductRulePageReqVO pageReqVO) {
        return storeProductRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<StoreProductRuleDO> getStoreProductRuleList(StoreProductRuleExportReqVO exportReqVO) {
        return storeProductRuleMapper.selectList(exportReqVO);
    }



}
