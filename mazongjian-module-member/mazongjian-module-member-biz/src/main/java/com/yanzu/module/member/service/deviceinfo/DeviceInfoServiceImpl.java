package com.yanzu.module.member.service.deviceinfo;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.deviceinfo.DeviceInfoConvert;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 设备管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DeviceInfoServiceImpl implements DeviceInfoService {

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Override
    public Long createDeviceInfo(DeviceInfoCreateReqVO createReqVO) {
        // 插入
        DeviceInfoDO deviceInfo = DeviceInfoConvert.INSTANCE.convert(createReqVO);
        deviceInfoMapper.insert(deviceInfo);
        // 返回
        return deviceInfo.getDeviceId();
    }

    @Override
    public void updateDeviceInfo(DeviceInfoUpdateReqVO updateReqVO) {
        // 校验存在
        validateDeviceInfoExists(updateReqVO.getDeviceId());
        // 更新
        DeviceInfoDO updateObj = DeviceInfoConvert.INSTANCE.convert(updateReqVO);
        deviceInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteDeviceInfo(Long id) {
        // 校验存在
        validateDeviceInfoExists(id);
        // 删除
        deviceInfoMapper.deleteById(id);
    }

    private void validateDeviceInfoExists(Long id) {
        if (deviceInfoMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public DeviceInfoDO getDeviceInfo(Long id) {
        return deviceInfoMapper.selectById(id);
    }

    @Override
    public List<DeviceInfoDO> getDeviceInfoList(Collection<Long> ids) {
        return deviceInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DeviceInfoDO> getDeviceInfoPage(DeviceInfoPageReqVO pageReqVO) {
        return deviceInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeviceInfoDO> getDeviceInfoList(DeviceInfoExportReqVO exportReqVO) {
        return deviceInfoMapper.selectList(exportReqVO);
    }

}
