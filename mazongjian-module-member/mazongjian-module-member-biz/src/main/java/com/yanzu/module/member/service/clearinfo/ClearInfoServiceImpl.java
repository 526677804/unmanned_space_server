package com.yanzu.module.member.service.clearinfo;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.clearinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.clearinfo.ClearInfoConvert;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 保洁信息管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ClearInfoServiceImpl implements ClearInfoService {

    @Resource
    private ClearInfoMapper clearInfoMapper;

    @Override
    public void updateClearInfo(ClearInfoUpdateReqVO updateReqVO) {
        // 更新
        ClearInfoDO updateObj = ClearInfoConvert.INSTANCE.convert(updateReqVO);
        clearInfoMapper.updateById(updateObj);
    }

    @Override
    public ClearInfoDO getClearInfo(Long id) {
        return clearInfoMapper.selectById(id);
    }

    @Override
    public List<ClearInfoDO> getClearInfoList(Collection<Long> ids) {
        return clearInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ClearInfoDO> getClearInfoPage(ClearInfoPageReqVO pageReqVO) {
        return clearInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ClearInfoDO> getClearInfoList(ClearInfoExportReqVO exportReqVO) {
        return clearInfoMapper.selectList(exportReqVO);
    }

}
