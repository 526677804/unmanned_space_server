package com.yanzu.module.member.service.deviceinfo;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.DeviceInfoCreateReqVO;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.DeviceInfoExportReqVO;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.DeviceInfoPageReqVO;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.DeviceInfoUpdateReqVO;
import com.yanzu.module.member.convert.deviceinfo.DeviceInfoConvert;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.service.iot.IotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DATA_NOT_EXISTS;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DEVICE_REG_ERROR;

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

    @Resource
    private IotService iotService;

    @Override
    @Transactional
    public Long createDeviceInfo(DeviceInfoCreateReqVO createReqVO) {
        //注册到平台
        boolean flag = false;
        switch (createReqVO.getType()) {
            case 1:
                //门禁
                iotService.regV1(createReqVO.getDeviceSn());
                break;
            case 2:
            case 3:
                //空开和云喇叭
                iotService.regV2(createReqVO.getDeviceSn());
                break;
        }
        if (!flag) {
            throw exception(DEVICE_REG_ERROR);
        }
        // 插入
        DeviceInfoDO deviceInfo = DeviceInfoConvert.INSTANCE.convert(createReqVO);
        deviceInfoMapper.insert(deviceInfo);
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
