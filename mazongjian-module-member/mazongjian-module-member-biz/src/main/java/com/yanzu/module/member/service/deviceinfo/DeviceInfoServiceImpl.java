package com.yanzu.module.member.service.deviceinfo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;
import com.yanzu.module.member.convert.deviceinfo.DeviceInfoConvert;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.iot.EwlService;
import com.yanzu.module.member.service.iot.IotService;
import com.yanzu.module.member.service.iot.TTLockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DATA_NOT_EXISTS;

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
    private EwlService ewlService;

    @Resource
    private TTLockService ttLockService;


    @Override
    @Transactional
    public Long createDeviceInfo(DeviceInfoCreateReqVO createReqVO) {
        // 插入
        DeviceInfoDO deviceInfo = DeviceInfoConvert.INSTANCE.convert(createReqVO);
        //如果是密码锁 需要获取一下锁的数据
        if (createReqVO.getType().compareTo(AppEnum.device_type.LOCK.getValue()) == 0) {
            String key = ttLockService.getKey(Integer.valueOf(createReqVO.getDeviceSn()));
            deviceInfo.setDeviceData(key);
        }
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
    @Transactional
    public void bind(DeviceInfoBindReqVO reqVO) {
        //如果设备已经被绑定了， 就更新绑定关系
        DeviceInfoDO deviceInfoDO = deviceInfoMapper.selectById(reqVO.getDeviceId());
        if (ObjectUtils.isEmpty(deviceInfoDO)) {
            throw exception(DATA_NOT_EXISTS);
        }
        deviceInfoDO.setStoreId(reqVO.getStoreId());
        deviceInfoDO.setRoomId(reqVO.getRoomId());
        deviceInfoMapper.updateById(deviceInfoDO);
    }

    @Override
    public String ewelinkScope() {
        return ewlService.getAuthUrl();
    }

}
