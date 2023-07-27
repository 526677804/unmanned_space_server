package com.yanzu.module.member.service.deviceuseinfo;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.deviceuseinfo.DeviceUseInfoConvert;
import com.yanzu.module.member.dal.mysql.deviceuseinfo.DeviceUseInfoMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 设备使用记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DeviceUseInfoServiceImpl implements DeviceUseInfoService {

    @Resource
    private DeviceUseInfoMapper deviceUseInfoMapper;


    @Override
    public DeviceUseInfoDO getDeviceUseInfo(Long id) {
        return deviceUseInfoMapper.selectById(id);
    }

    @Override
    public List<DeviceUseInfoDO> getDeviceUseInfoList(Collection<Long> ids) {
        return deviceUseInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DeviceUseInfoDO> getDeviceUseInfoPage(DeviceUseInfoPageReqVO pageReqVO) {
        return deviceUseInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeviceUseInfoDO> getDeviceUseInfoList(DeviceUseInfoExportReqVO exportReqVO) {
        return deviceUseInfoMapper.selectList(exportReqVO);
    }

}
