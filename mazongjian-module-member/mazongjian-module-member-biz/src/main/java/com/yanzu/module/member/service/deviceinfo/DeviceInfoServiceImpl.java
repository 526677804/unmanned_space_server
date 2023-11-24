package com.yanzu.module.member.service.deviceinfo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;
import com.yanzu.module.member.convert.deviceinfo.DeviceInfoConvert;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.iot.IotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

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

    @Resource
    private RoomInfoMapper roomInfoMapper;
    @Resource
    private IotService iotService;

    @Override
    @Transactional
    public Long createDeviceInfo(DeviceInfoCreateReqVO createReqVO) {
        //注册到平台
//        boolean flag = false;
//        switch (createReqVO.getType()) {
//            case 1:
//                //门禁
//                flag = iotService.regV1(createReqVO.getDeviceSn());
//                break;
//            case 2:
//            case 3:
//                //空开和云喇叭
//                flag = iotService.regV2(createReqVO.getDeviceSn());
//                break;
//        }
//        if (!flag) {
//            throw exception(DEVICE_REG_ERROR);
//        }
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
    public PageResult<DeviceInfoRespVO> getDeviceInfoPage(DeviceInfoPageReqVO pageReqVO) {
        PageHelper.startPage(pageReqVO);
        List<DeviceInfoRespVO> list = deviceInfoMapper.getDeviceInfoPage(pageReqVO);
        PageInfo<DeviceInfoRespVO> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public List<DeviceInfoDO> getDeviceInfoList(DeviceInfoExportReqVO exportReqVO) {
        return deviceInfoMapper.selectList(exportReqVO);
    }

    @Override
    public void configWifi(Long deviceId) {
        //获取设备的sn
        String sn = deviceInfoMapper.selectById(deviceId).getDeviceSn();
        if (!ObjectUtils.isEmpty(sn)) {
            boolean flag = iotService.runKongkai(sn, "wifi_config");
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }

    @Override
    @Transactional
    public void bind(DeviceInfoBindReqVO reqVO) {
        //如果设备已经被绑定了， 就不允许绑定
        DeviceInfoDO deviceInfoDO = deviceInfoMapper.selectById(reqVO.getDeviceId());
        if (!ObjectUtils.isEmpty(deviceInfoDO.getStoreId())) {
            throw exception(DEVICE_BIND_ERROR);
        }
        deviceInfoDO.setStoreId(reqVO.getStoreId());
        deviceInfoDO.setRoomId(reqVO.getRoomId());
        deviceInfoMapper.updateById(deviceInfoDO);
        if (!ObjectUtils.isEmpty(reqVO.getRoomId())) {
            //绑定的房间  如果房间状态为禁用，则改成启用
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(reqVO.getRoomId());
            if (roomInfoDO.getStatus().compareTo(AppEnum.room_status.DISABLE.getValue()) == 0) {
                roomInfoDO.setStatus(AppEnum.room_status.ENABLE.getValue());
                roomInfoMapper.updateById(roomInfoDO);
            }
        }

    }

}
