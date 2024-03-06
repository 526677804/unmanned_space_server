package com.yanzu.module.member.service.franchiseinfo;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.franchiseinfo.FranchiseInfoConvert;
import com.yanzu.module.member.dal.mysql.franchiseinfo.FranchiseInfoMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 加盟信息 Service 实现类
 *
 * @author yanzu
 */
@Service
@Validated
public class FranchiseInfoServiceImpl implements FranchiseInfoService {

    @Resource
    private FranchiseInfoMapper franchiseInfoMapper;

    @Override
    public void updateFranchiseInfo(FranchiseInfoUpdateReqVO updateReqVO) {
        // 校验存在
        validateFranchiseInfoExists(updateReqVO.getId());
        // 更新
        FranchiseInfoDO updateObj = FranchiseInfoConvert.INSTANCE.convert(updateReqVO);
        franchiseInfoMapper.updateById(updateObj);
    }
    @Override
    public void deleteFranchiseInfo(Long id) {
        // 校验存在
        validateFranchiseInfoExists(id);
        // 删除
        franchiseInfoMapper.deleteById(id);
    }

    private void validateFranchiseInfoExists(Long id) {
        if (franchiseInfoMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public FranchiseInfoDO getFranchiseInfo(Long id) {
        return franchiseInfoMapper.selectById(id);
    }

    @Override
    public List<FranchiseInfoDO> getFranchiseInfoList(Collection<Long> ids) {
        return franchiseInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<FranchiseInfoDO> getFranchiseInfoPage(FranchiseInfoPageReqVO pageReqVO) {
        return franchiseInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<FranchiseInfoDO> getFranchiseInfoList(FranchiseInfoExportReqVO exportReqVO) {
        return franchiseInfoMapper.selectList(exportReqVO);
    }

}
