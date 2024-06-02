package com.yanzu.module.member.service.deviceinfo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;
import com.yanzu.module.member.convert.deviceinfo.DeviceInfoConvert;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.iot.IotService;
import com.yanzu.module.member.service.iot.iotBean.IotDeviceBaseVO;
import com.yanzu.module.member.service.iot.iotBean.IotDeviceConfigWifiReqVO;
import com.yanzu.module.member.service.iot.iotBean.IotDeviceSetAutoLockReqVO;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
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
    private IotService iotService;

    @Resource
    private StoreInfoService storeInfoService;


    @Override
    @Transactional
    public void createDeviceInfo(DeviceInfoCreateReqVO createReqVO) {
        //新增的设备不能存在
        int i = deviceInfoMapper.countBySN(createReqVO.getDeviceSn());
        if (i > 0) {
            throw exception(DEVICE_DATA_EXISTS_ERROR);
        }
        //先在iot平台绑定设备
        String data = iotService.bind(createReqVO.getDeviceSn());
        // 插入
        DeviceInfoDO deviceInfo = DeviceInfoConvert.INSTANCE.convert(createReqVO);
        deviceInfo.setDeviceData(data);
        deviceInfoMapper.insert(deviceInfo);

    }


    @Override
    @Transactional
    public void deleteDeviceInfo(Long id) {
        DeviceInfoDO deviceInfoDO = deviceInfoMapper.selectById(id);
        //只能操作自己的设备
        if (!ObjectUtils.isEmpty(deviceInfoDO) && deviceInfoDO.getCreator().equals(String.valueOf(getLoginUserId()))) {
            // 先解绑
            iotService.unbind(deviceInfoDO.getDeviceSn());
            // 删除
            deviceInfoMapper.deleteById(id);

        }
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
        //检查权限
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
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
    public void iotScope() {
        iotService.authorize();
    }

    @Override
    public void configWifi(DeviceInfoConfigWifiReqVO reqVO) {
        DeviceInfoDO deviceInfoDO = deviceInfoMapper.selectById(reqVO.getDeviceId());
        //只能操作自己的设备
        if (!ObjectUtils.isEmpty(deviceInfoDO) && deviceInfoDO.getCreator().equals(String.valueOf(getLoginUserId()))) {
            IotDeviceConfigWifiReqVO vo = new IotDeviceConfigWifiReqVO();
            vo.setDeviceSn(deviceInfoDO.getDeviceSn());
            vo.setSsid(reqVO.getSsid());
            vo.setPasswd(reqVO.getPasswd());
            iotService.configWifi(vo);
        }
    }

    @Override
    public void setLockAutoLock(DeviceInfoSetAutoLockReqVO reqVO) {
        DeviceInfoDO deviceInfoDO = deviceInfoMapper.selectById(reqVO.getDeviceId());
        //只能操作自己的设备
        if (!ObjectUtils.isEmpty(deviceInfoDO) && deviceInfoDO.getCreator().equals(String.valueOf(getLoginUserId()))) {
            IotDeviceSetAutoLockReqVO vo = new IotDeviceSetAutoLockReqVO();
            vo.setDeviceSn(deviceInfoDO.getDeviceSn());
            vo.setSecend(reqVO.getSecend());
            iotService.setLockAutoLock(vo);
        }

    }
}
