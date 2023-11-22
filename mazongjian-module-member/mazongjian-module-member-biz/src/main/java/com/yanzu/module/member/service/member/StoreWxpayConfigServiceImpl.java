package com.yanzu.module.member.service.member;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.member.vo.*;
import com.yanzu.module.member.convert.member.StoreWxpayConfigConvert;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;
import com.yanzu.module.member.dal.mysql.member.StoreWxpayConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DATA_NOT_EXISTS;

/**
 * 门店微信支付配置 Service 实现类
 *
 * @author MrGuan
 */
@Service
@Validated
public class StoreWxpayConfigServiceImpl implements StoreWxpayConfigService {

    @Resource
    private StoreWxpayConfigMapper storeWxpayConfigMapper;

    @Override
    @Transactional
    public Long createStoreWxpayConfig(StoreWxpayConfigCreateReqVO createReqVO) {
        // 插入
        StoreWxpayConfigDO storeWxpayConfig = StoreWxpayConfigConvert.INSTANCE.convert(createReqVO);
        storeWxpayConfigMapper.insert(storeWxpayConfig);
        // 返回
        return storeWxpayConfig.getId();
    }

    @Override
    @Transactional
    public void updateStoreWxpayConfig(StoreWxpayConfigUpdateReqVO updateReqVO) {
        // 校验存在
        validateStoreWxpayConfigExists(updateReqVO.getId());
        // 更新
        StoreWxpayConfigDO updateObj = StoreWxpayConfigConvert.INSTANCE.convert(updateReqVO);
        storeWxpayConfigMapper.updateById(updateObj);
    }

    @Override
    @Transactional
    public void deleteStoreWxpayConfig(Long id) {
        // 校验存在
        validateStoreWxpayConfigExists(id);
        // 删除
        storeWxpayConfigMapper.deleteById(id);
    }

    private void validateStoreWxpayConfigExists(Long id) {
        if (storeWxpayConfigMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public StoreWxpayConfigDO getStoreWxpayConfig(Long id) {
        return storeWxpayConfigMapper.selectById(id);
    }

    @Override
    public List<StoreWxpayConfigDO> getStoreWxpayConfigList(Collection<Long> ids) {
        return storeWxpayConfigMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<StoreWxpayConfigPageRespVO> getStoreWxpayConfigPage(StoreWxpayConfigPageReqVO pageReqVO) {
        PageHelper.startPage(pageReqVO);
        List<StoreWxpayConfigPageRespVO> list = storeWxpayConfigMapper.getStoreWxpayConfigPage(pageReqVO);
        PageInfo<StoreWxpayConfigPageRespVO> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public List<StoreWxpayConfigDO> getStoreWxpayConfigList(StoreWxpayConfigExportReqVO exportReqVO) {
        return storeWxpayConfigMapper.selectList(exportReqVO);
    }

}
