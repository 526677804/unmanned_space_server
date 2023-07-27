package com.yanzu.module.member.service.storeinfo;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.storeinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.storeinfo.StoreInfoConvert;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 门店管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class StoreInfoServiceImpl implements StoreInfoService {

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Override
    public Long createStoreInfo(StoreInfoCreateReqVO createReqVO) {
        // 插入
        StoreInfoDO storeInfo = StoreInfoConvert.INSTANCE.convert(createReqVO);
        storeInfoMapper.insert(storeInfo);
        // 返回
        return storeInfo.getStoreId();
    }

    @Override
    public void updateStoreInfo(StoreInfoUpdateReqVO updateReqVO) {
        // 校验存在
        validateStoreInfoExists(updateReqVO.getStoreId());
        // 更新
        StoreInfoDO updateObj = StoreInfoConvert.INSTANCE.convert(updateReqVO);
        storeInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteStoreInfo(Long id) {
        // 校验存在
        validateStoreInfoExists(id);
        // 删除
        storeInfoMapper.deleteById(id);
    }

    private void validateStoreInfoExists(Long id) {
        if (storeInfoMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public StoreInfoDO getStoreInfo(Long id) {
        return storeInfoMapper.selectById(id);
    }

    @Override
    public List<StoreInfoDO> getStoreInfoList(Collection<Long> ids) {
        return storeInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<StoreInfoDO> getStoreInfoPage(StoreInfoPageReqVO pageReqVO) {
        return storeInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<StoreInfoDO> getStoreInfoList(StoreInfoExportReqVO exportReqVO) {
        return storeInfoMapper.selectList(exportReqVO);
    }

}
