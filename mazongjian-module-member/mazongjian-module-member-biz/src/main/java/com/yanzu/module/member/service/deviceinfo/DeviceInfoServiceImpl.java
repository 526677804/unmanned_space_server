package com.yanzu.module.member.service.deviceinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;
import com.yanzu.module.member.convert.deviceinfo.DeviceInfoConvert;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.iot.IotDeviceService;
import com.yanzu.module.member.service.iot.device.IotDeviceBaseVO;
import com.yanzu.module.member.service.iot.device.IotDeviceConfigWifiReqVO;
import com.yanzu.module.member.service.iot.device.IotDeviceContrlReqVO;
import com.yanzu.module.member.service.iot.device.IotDeviceSetAutoLockReqVO;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private IotDeviceService iotDeviceService;

    @Resource
    private StoreInfoService storeInfoService;


    @Override
    @Transactional
    public void createDeviceInfo(DeviceInfoCreateReqVO createReqVO) {
        //如果不是共用设备  那新增的设备不能存在
        if (!createReqVO.getShare()) {
            int i = deviceInfoMapper.countBySN(createReqVO.getDeviceSn());
            if (i > 0) {
                throw exception(DEVICE_DATA_EXISTS_ERROR);
            }
        }
        //有的设备每个房间只能存在一个
        if (!ObjectUtils.isEmpty(createReqVO.getRoomId())) {
            switch (createReqVO.getType()) {
                case 1:
                case 3:
                case 5:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    int c = deviceInfoMapper.countByTypeAndRoomId(createReqVO.getType(), createReqVO.getRoomId());
                    if (c > 0) {
                        throw exception(DEVICE_ADD_MAX_NUM_ERROR);
                    }
                    break;
            }
        }
        //先在iot平台绑定设备
        String data = iotDeviceService.bind(createReqVO.getDeviceSn());
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
            //如果是多房间共用  只有全部删除绑定关系时才去解绑
            if (deviceInfoDO.getShare()) {
                if (deviceInfoMapper.countBySN(deviceInfoDO.getDeviceSn()) == 1) {
                    //解绑
                    iotDeviceService.unbind(deviceInfoDO.getDeviceSn());
                }
            } else {
                //解绑
                iotDeviceService.unbind(deviceInfoDO.getDeviceSn());
            }
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
    public PageResult<DeviceInfoRespVO> getDeviceInfoPage(DeviceInfoPageReqVO reqVO, boolean isAdmin) {
        //检查权限
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        IPage<DeviceInfoRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        deviceInfoMapper.getDeviceInfoPage(page, reqVO, getLoginUserId(), isAdmin);
        return new PageResult<>(page.getRecords(), page.getTotal());
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
        //有的设备每个房间只能存在一个
        if (!ObjectUtils.isEmpty(reqVO.getRoomId()) && reqVO.getRoomId().compareTo(deviceInfoDO.getRoomId()) != 0) {
            switch (deviceInfoDO.getType()) {
                case 1:
                case 3:
                case 5:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    int c = deviceInfoMapper.countByTypeAndRoomId(deviceInfoDO.getType(), reqVO.getRoomId());
                    if (c > 0) {
                        throw exception(DEVICE_ADD_MAX_NUM_ERROR);
                    }
                    break;
            }
        }
        deviceInfoMapper.updateBindInfo(deviceInfoDO.getDeviceId(), reqVO.getRoomId());
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
            iotDeviceService.configWifi(vo);
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
            iotDeviceService.setLockAutoLock(vo);
        }

    }

    @Override
    public void control(DeviceControlReqVO reqVO) {
        DeviceInfoDO deviceInfoDO = deviceInfoMapper.selectById(reqVO.getDeviceId());
        //只能操作自己的设备
        if (!ObjectUtils.isEmpty(deviceInfoDO) && deviceInfoDO.getCreator().equals(String.valueOf(getLoginUserId()))) {
            IotDeviceBaseVO<IotDeviceContrlReqVO> vo = new IotDeviceBaseVO();
            List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
            IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
            iotDeviceContrlReqVO.setOutlet(0).setCmd(reqVO.getCmd());
            param.add(iotDeviceContrlReqVO);
            vo.setDeviceSn(deviceInfoDO.getDeviceSn()).setParams(param);
            boolean flag = iotDeviceService.control(vo);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }
}
