package com.yanzu.module.member.service.device;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.deviceuseinfo.DeviceUseInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.service.iot.IotService;
import com.yanzu.module.member.service.iot.iotBean.IotDeviceBaseVO;
import com.yanzu.module.member.service.iot.iotBean.IotDeviceContrlReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DEVICE_OPRATION_ERROR;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.device
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/28 12:13
 */
@Service
@Validated
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceUseInfoMapper deviceUseInfoMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private IotService iotService;

    @Override
    @Transactional
    public void openStoreDoor(Long userId, Long storeId, int type) {
        //1用户开门 2管理员开门 3保洁开门
        switch (type) {
            case 1://1用户开门
                openStoreDoor(storeId);
                break;
            case 2://2管理员开门
            case 3://3保洁开门
                //这里是开门店的大门，所以随便开
                openStoreDoor(storeId);
                break;
            case 4:
                break;
        }
        //增加开门记录
        saveDeviceUseRecord(userId, storeId, null, "openStoreDoor");
    }


    /**
     * 保存设备操作记录
     * @param userId
     * @param storeId
     * @param roomId
     * @param cmd
     */
    private void saveDeviceUseRecord(Long userId, Long storeId, Long roomId, String cmd) {
        DeviceUseInfoDO deviceUseInfoDO = new DeviceUseInfoDO();
        deviceUseInfoDO.setUserId(userId);
        deviceUseInfoDO.setStoreId(storeId);
        deviceUseInfoDO.setRoomId(roomId);
        deviceUseInfoDO.setCmd(cmd);
        deviceUseInfoMapper.insert(deviceUseInfoDO);
    }


    private void openStoreDoor(Long storeId) {
        //获取大门的门禁sn
        String sn = deviceInfoMapper.getSnByStoreId(storeId);
        openDoor(sn);
    }


    /**
     * 开大门
     *
     * @param sn
     */
    private void openDoor(String sn) {
        if (!ObjectUtils.isEmpty(sn)) {
            IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
            List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
            IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
            iotDeviceContrlReqVO.setOutlet(0).setCmd("on");
            reqVO.setDeviceSn(sn).setParams(param);
            boolean flag = iotService.control(reqVO);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }

    /**
     * 开房间门
     *
     * @param roomId
     */
    private void openRoomDoor(Long roomId) {
        //开门 等于是开门+通电
        //获取房间设备的sn 1=门禁 2=空开 4=灯具 5=密码锁 6=网关
        String sn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 2);
        opSwitch(sn, "on");
        //可能有门禁  获取一下门禁
        String doorSn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 1);
        openDoor(doorSn);
        //可能有灯具
        String lightSn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 4);
        opSwitch(lightSn, "on");
    }

    /**
     * 打开开关
     *
     * @param sn
     */
    private void opSwitch(String sn, String cmd) {
        if (!ObjectUtils.isEmpty(sn)) {
            IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
            List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
            IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
            iotDeviceContrlReqVO.setOutlet(0).setCmd(cmd);
            reqVO.setDeviceSn(sn).setParams(param);
            boolean flag = iotService.control(reqVO);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }

    private void closeRoomDoor(Long roomId) {
        //获取房间空开设备的sn
        String kongKaiSN = deviceInfoMapper.getSnByRoomIdAndType(roomId, 2);
        if (!ObjectUtils.isEmpty(kongKaiSN)) {
            opSwitch(kongKaiSN, "off");
        }
    }

    @Override
    @Transactional
    public void openRoomDoor(Long userId, Long storeId, Long roomId, int type) {
        if (!ObjectUtils.isEmpty(roomId) && ObjectUtils.isEmpty(storeId)) {
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
            storeId = roomInfoDO.getStoreId();
        }
        //1用户开门 2管理员开门 3保洁开门 4系统开门
        switch (type) {
            case 1://1用户开门 这里的 roomId肯定不是空
                openRoomDoor(roomId);
                break;
            case 2:
            case 3:
                //管理员不限制 保洁开门权限在调用处控制 只能开待清洁的房间
                openRoomDoor(roomId);
                break;
            case 4:
                openRoomDoor(roomId);
                break;
        }
        //增加开门记录
        saveDeviceUseRecord(userId, storeId, roomId, "openRoomDoor");
    }

    @Override
    @Transactional
    public void openRoomBlueLock(Long userId, Long storeId, Long roomId, int type) {
        if (!ObjectUtils.isEmpty(roomId) && ObjectUtils.isEmpty(storeId)) {
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
            storeId = roomInfoDO.getStoreId();
        }
        //1用户开门 2管理员开门 3保洁开门 4系统开门
        switch (type) {
            case 1://1用户开门 这里的 roomId肯定不是空
                openRoomDoor(roomId);
                break;
            case 2:
            case 3:
                //管理员不限制 保洁开门权限在调用处控制 只能开待清洁的房间
                openRoomDoor(roomId);
                break;
            case 4:
                openRoomDoor(roomId);
                break;
        }
        //增加开门记录
        saveDeviceUseRecord(userId, storeId, roomId, "开房间密码门锁");
    }

    @Override
    @Transactional
    public void closeRoomDoor(Long userId, Long storeId, Long roomId, int type) {
        if (!ObjectUtils.isEmpty(roomId) && ObjectUtils.isEmpty(storeId)) {
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
            storeId = roomInfoDO.getStoreId();
        }
        //1用户关 2管理员关 3保洁关  4系统关
        switch (type) {
            case 1://1用户关
                closeRoomDoor(roomId);
                break;
            case 2:
            case 3:
                //管理员 保洁也不限制关
                closeRoomDoor(roomId);
                //管理员关的 还要尝试关灯
                closeLightByRoomId(userId, storeId, roomId, 3);
                break;
            case 4:
                //系统关
                closeRoomDoor(roomId);
                break;
        }
        //增加记录
        saveDeviceUseRecord(userId, storeId, roomId, "closeRoomDoor");
    }

    //提示语类型 1欢迎语 2结束时间30分钟提醒  3结束时间15分钟提示  4 结束时间5分钟提醒
    @Override
    public void runSound(Long roomId, Integer type) {
        log.info("发送云喇叭提醒,房间id:{}", roomId);
        //获取设备的sn
        String sn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 3);
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        //获取音量设置
        if (!ObjectUtils.isEmpty(sn)) {
            IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
            List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
            IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
            iotDeviceContrlReqVO.setOutlet(0).setCmd(String.valueOf(type)).setType(roomInfoDO.getYunlabaSound());
            reqVO.setDeviceSn(sn).setParams(param);
            boolean flag = iotService.control(reqVO);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }


    @Override
    public void testYunlaba(Long roomId) {
        runSound(roomId, 1);
    }

    @Override
    public void closeLightByRoomId(Long userId, Long storeId, Long roomId, int type) {
        if (!ObjectUtils.isEmpty(roomId) && ObjectUtils.isEmpty(storeId)) {
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
            storeId = roomInfoDO.getStoreId();
        }
        //1用户关灯 2管理员关灯 3保洁关灯 4系统关灯
        switch (type) {
            case 1://1用户关灯 目前没有用户关灯的情况
                break;
            case 2:
            case 3://管理员和保洁目前也没有单独关灯的需求  都是关电的时候，同时关灯
                break;
            case 4:
                closeLight(roomId);
                break;
        }
        //增加关灯记录
        saveDeviceUseRecord(userId, storeId, roomId, "closeRoomLight");
    }

    @Override
    public void clearByRoomId(Long roomId) {
        deviceInfoMapper.update(new DeviceInfoDO().setRoomId(null).setStoreId(null),
                new LambdaUpdateWrapper<DeviceInfoDO>().eq(DeviceInfoDO::getRoomId, roomId)
        );
    }

    @Override
    public boolean bind(String sn) {
        return iotService.bind(sn);
    }

    @Override
    public boolean unbind(String sn) {
        return iotService.unbind(sn);
    }

    private void closeLight(Long roomId) {
        //获取房间设备的sn 1=门禁 2=空开 4=灯具 5=密码锁 6=网关
        String lightSn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 4);
        if (!StringUtils.isEmpty(lightSn)) {
            //关灯
            opSwitch(lightSn,"off");
        }
        //关灯的同时一定会关电  可能电已经关了，这里保守起见，再关一次
        //获取房间空开设备的sn
        String kongKaiSN = deviceInfoMapper.getSnByRoomIdAndType(roomId, 2);
        if (!ObjectUtils.isEmpty(kongKaiSN)) {
            opSwitch(kongKaiSN,"off");
        }
    }

}
