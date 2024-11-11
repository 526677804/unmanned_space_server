package com.yanzu.module.member.service.device;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yanzu.module.member.controller.app.order.vo.ControlKTReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppAddDeviceReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppRoomListVO;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.dataobject.storesound.StoreSoundInfoDO;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.deviceuseinfo.DeviceUseInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.dal.mysql.storesound.StoreSoundInfoMapper;
import com.yanzu.module.member.service.iot.IotDeviceService;
import com.yanzu.module.member.service.iot.device.IotControlKTReqVO;
import com.yanzu.module.member.service.iot.device.IotDeviceAddBlacklistReqVO;
import com.yanzu.module.member.service.iot.device.IotDeviceBaseVO;
import com.yanzu.module.member.service.iot.device.IotDeviceContrlReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.CLEAR_INFO_STATUS_OPRATION_ERROR;
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
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private ClearInfoMapper clearInfoMapper;


    @Resource
    private IotDeviceService iotDeviceService;


    @Resource
    private StoreSoundInfoMapper storeSoundInfoMapper;

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
        saveDeviceUseRecord(userId, storeId, null, "开门店大门");
    }


    /**
     * 保存设备操作记录
     *
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
        String sn = deviceInfoMapper.getSnByStoreIdAndType(storeId, 1);
        if (ObjectUtils.isEmpty(sn)) {
            //可能用的密码锁
            sn = deviceInfoMapper.getSnByStoreIdAndType(storeId, 5);
            if (!ObjectUtils.isEmpty(sn)) {
                IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
                List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
                IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
                iotDeviceContrlReqVO.setOutlet(0).setCmd("pulse");
                param.add(iotDeviceContrlReqVO);
                reqVO.setDeviceSn(sn).setParams(param);
                boolean flag = iotDeviceService.control(reqVO);
                if (!flag) {
                    throw exception(DEVICE_OPRATION_ERROR);
                }
            }
        } else {
            IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
            List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
            IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
            iotDeviceContrlReqVO.setOutlet(0).setCmd("pulse");
            param.add(iotDeviceContrlReqVO);
            reqVO.setDeviceSn(sn).setParams(param);
            boolean flag = iotDeviceService.control(reqVO);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }


    /**
     * 开门禁
     *
     * @param sn
     */
    private void openDoor(String sn, Long storeId) {
        if (!ObjectUtils.isEmpty(sn)) {
            boolean orderDoorOpen = storeInfoMapper.getOrderDoorOpen(storeId);
            IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
            List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
            IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
            iotDeviceContrlReqVO.setOutlet(0).setCmd(orderDoorOpen ? "on" : "pulse");
            param.add(iotDeviceContrlReqVO);
            reqVO.setDeviceSn(sn).setParams(param);
            boolean flag = iotDeviceService.control(reqVO);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }

    /**
     * 关门禁
     * 2024年11月5日 修改  关门失败 不影响订单结束
     *
     * @param sn
     */
    private void closeDoor(String sn) {
        if (!ObjectUtils.isEmpty(sn)) {
            IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
            List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
            IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
            iotDeviceContrlReqVO.setOutlet(0).setCmd("off");
            param.add(iotDeviceContrlReqVO);
            reqVO.setDeviceSn(sn).setParams(param);
            try {
                iotDeviceService.control(reqVO);
            } catch (Exception e) {
            }
        }
    }


    /**
     * 开房间门
     *
     * @param roomId
     */
    @Async
    protected void openRoomDoor(Long storeId, Long roomId) {
        //开门 等于是开门+通电 把房间内所有设备操作一遍
        List<DeviceInfoDO> deviceList = deviceInfoMapper.getByRoomId(roomId);
        if (!CollectionUtils.isEmpty(deviceList)) {
            //1=门禁 2=空开 3=云喇叭 4=灯具 5=密码锁 6=网关 7=插座 8=锁球器控制器（12V） 9=人脸门禁机  10=智能语音喇叭 11=二维码识别器 12=红外控制器 13=三路控制器
            deviceList.forEach(x -> {
                switch (x.getType().intValue()) {
                    case 1:
                    case 9:
                        openDoor(x.getDeviceSn(), storeId);
                        break;
                    case 2:
                    case 4:
                    case 7:
                    case 8:
                        opSwitch(x.getDeviceSn(), "on");
                        break;
                    case 5:
                        //如果有网关 就尝试网关开锁
                        if (countGateway(storeId) > 0) {
                            openDoor(x.getDeviceSn(), storeId);
                        }
                        break;
                    case 6:
                        break;
                    case 13:
                        //三路控制器 开的时候三路全开 注意判断是否需要门禁常开
                        boolean orderDoorOpen = storeInfoMapper.getOrderDoorOpen(storeId);
                        IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
                        List<IotDeviceContrlReqVO> param = new ArrayList<>(3);
                        param.add(new IotDeviceContrlReqVO().setOutlet(0).setCmd("on"));
                        param.add(new IotDeviceContrlReqVO().setOutlet(1).setCmd("on"));
                        //第三路就是门禁用
                        param.add(new IotDeviceContrlReqVO().setOutlet(2).setCmd(orderDoorOpen ? "on" : "pulse"));
                        reqVO.setDeviceSn(x.getDeviceSn()).setParams(param);
                        boolean flag = iotDeviceService.control(reqVO);
                        if (!flag) {
                            throw exception(DEVICE_OPRATION_ERROR);
                        }
                        break;
                }

            });
        }
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
            param.add(iotDeviceContrlReqVO);
            reqVO.setDeviceSn(sn).setParams(param);
            boolean flag = iotDeviceService.control(reqVO);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }

    private void closeRoomDoor(Long storeId, Long roomId) {
        //关门断电 把房间内所有设备操作一遍   关灯不在这里处理  因为有延时关灯的功能
        List<DeviceInfoDO> deviceList = deviceInfoMapper.getByRoomId(roomId);
        if (!CollectionUtils.isEmpty(deviceList)) {
            //1=门禁 2=空开 3=云喇叭 4=灯具 5=密码锁 6=网关 7=插座 8=锁球器控制器（12V） 9=人脸门禁机  10=智能语音喇叭 11=二维码识别器 12=红外控制器 13=三路控制器
            deviceList.forEach(x -> {
                //如果该设备是共用设备，必须绑定的所有房间都不存在订单时，才允许关闭
                if (x.getShare()) {
                    //先关闭设备  后结束订单  所以如果存在1个订单以上 就直接退出关闭该设备
                    int i = deviceInfoMapper.countShare(x.getDeviceSn());
                    if (i > 1) {
                        return;
                    }
                }
                switch (x.getType().intValue()) {
                    case 1:
                    case 9:
                        closeDoor(x.getDeviceSn());
                        break;
                    case 2:
                    case 7:
                    case 8:
                        opSwitch(x.getDeviceSn(), "off");
                        break;
                    case 5:
                        //如果有网关 就尝试网关关锁
                        if (countGateway(storeId) > 0) {
                            closeDoor(x.getDeviceSn());
                        }
                        break;
                    case 6:
                        break;
                    case 13:
                        //三路控制器 关的时候先不关灯
                        IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
                        List<IotDeviceContrlReqVO> param = new ArrayList<>(2);
                        param.add(new IotDeviceContrlReqVO().setOutlet(0).setCmd("off"));
                        param.add(new IotDeviceContrlReqVO().setOutlet(2).setCmd("off"));
                        reqVO.setDeviceSn(x.getDeviceSn()).setParams(param);
                        boolean flag = iotDeviceService.control(reqVO);
                        if (!flag) {
                            throw exception(DEVICE_OPRATION_ERROR);
                        }
                        break;

                }
            });
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
                openRoomDoor(storeId, roomId);
                break;
            case 2:
                //管理员不限制
                openRoomDoor(storeId, roomId);
                break;
            case 3:
                //保洁开门
//                getClearInfoByRoom(userId, roomId);
                openRoomDoor(storeId, roomId);
                break;
            case 4:
                openRoomDoor(storeId, roomId);
                break;
        }
        //增加开门记录
        saveDeviceUseRecord(userId, storeId, roomId, "打开房间电源");
    }


    private ClearInfoDO getClearInfoByRoom(Long userId, Long roomId) {
        ClearInfoDO clearInfoDO = clearInfoMapper.getCurrent(userId, roomId);
        if (ObjectUtils.isEmpty(clearInfoDO)) {
            throw exception(CLEAR_INFO_STATUS_OPRATION_ERROR);
        }
        return clearInfoDO;
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
                openRoomDoor(storeId, roomId);
                break;
            case 2:
                //管理员不限制
                openRoomDoor(storeId, roomId);
                break;
            case 3:
                //保洁
//                getClearInfoByRoom(userId, roomId);
                openRoomDoor(storeId, roomId);
                break;
            case 4:
                openRoomDoor(storeId, roomId);
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
            case 1:
                //1用户关
            case 2:
                //管理员 不限制关
                closeRoomDoor(storeId, roomId);
                //还要尝试关灯
                closeLightByRoomId(userId, storeId, roomId, 2);
                break;
            case 3:
                //保洁
//                getClearInfoByRoom(userId, roomId);
                closeRoomDoor(storeId, roomId);
                //还要尝试关灯
                closeLightByRoomId(userId, storeId, roomId, 3);
                break;
            case 4:
                //系统关
                closeRoomDoor(storeId, roomId);
                break;
        }
        //增加记录
        saveDeviceUseRecord(userId, storeId, roomId, "关闭房间电源");
    }

    //提示语类型 1欢迎语 2结束时间30分钟提醒  3结束时间15分钟提示  4 结束时间5分钟提醒
    @Override
    public void runSound(Long roomId, Integer type) {
        log.info("发送云喇叭提醒,房间id:{}", roomId);
        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
        //1=门禁 2=空开 3=云喇叭 4=灯具 5=密码锁 6=网关 7=插座 8=锁球器控制器（12V） 9=人脸门禁机  10=智能语音喇叭 11=二维码识别器 12=红外控制器 13=三路控制器
        //获取设备的sn
        List<DeviceInfoDO> deviceList = deviceInfoMapper.getByRoomIdAndType(roomId, new Integer[]{3, 10, 13});
        if (!CollectionUtils.isEmpty(deviceList)) {
            String cmd = String.valueOf(type);
            //获取是否存在自定义播报文字
            StoreSoundInfoDO soundInfoDO = storeSoundInfoMapper.getByStoreId(roomInfoDO.getStoreId());
            if (!ObjectUtils.isEmpty(soundInfoDO)) {
                switch (type) {
                    case 1:
                        if (!StringUtils.isEmpty(soundInfoDO.getWelcomeText())) {
                            cmd = soundInfoDO.getWelcomeText();
                        }
                        break;
                    case 2:
                        if (!StringUtils.isEmpty(soundInfoDO.getEndText30())) {
                            cmd = soundInfoDO.getEndText30();
                        }
                        break;
                    case 4:
                        if (!StringUtils.isEmpty(soundInfoDO.getEndText5())) {
                            cmd = soundInfoDO.getEndText5();
                        }
                        break;
                    case 5:
                        if (!StringUtils.isEmpty(soundInfoDO.getNightText())) {
                            cmd = soundInfoDO.getNightText();
                        }
                        break;
                }
            }
            for (DeviceInfoDO x : deviceList) {
                IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
                List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
                IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
                iotDeviceContrlReqVO.setOutlet(0).setCmd(cmd).setType(roomInfoDO.getYunlabaSound());
                param.add(iotDeviceContrlReqVO);
                reqVO.setDeviceSn(x.getDeviceSn()).setParams(param);
                boolean flag = iotDeviceService.control(reqVO);
                if (!flag) {
                    throw exception(DEVICE_OPRATION_ERROR);
                }
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
            case 3://管理员和保洁关灯
                closeLight(roomId);
                break;
            case 4:
                closeLight(roomId);
                break;
        }
        //增加关灯记录
        saveDeviceUseRecord(userId, storeId, roomId, "关灯");
    }

    @Override
    public void clearByRoomId(Long roomId) {
        deviceInfoMapper.update(new DeviceInfoDO().setRoomId(null).setStoreId(null),
                new LambdaUpdateWrapper<DeviceInfoDO>().eq(DeviceInfoDO::getRoomId, roomId)
        );
    }

    @Override
    public int countGateway(Long storeId) {
        return deviceInfoMapper.countGateway(storeId);
    }

    @Override
    public int countKongtiao(Long roomId) {
        return deviceInfoMapper.countKongtiao(roomId);
    }

    @Override
    public String addUserFace(Long storeId, String photoUrl, String remark) {
        return iotDeviceService.addUserFace(storeId, photoUrl, remark);
    }

    @Override
    public void delUserFace(Long storeId, String admitGuid) {
        iotDeviceService.delUserFace(storeId, admitGuid);
    }

    @Override
    public void controlKT(String cmd, Long storeId, Long roomId) {
        //获取房间设备的sn  10=智能语音喇叭（带红外控制）  12=红外控制器  13=三路控制器
        List<DeviceInfoDO> list = deviceInfoMapper.getIRListByRoomId(roomId);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(x -> {
                IotControlKTReqVO req = new IotControlKTReqVO();
                req.setDeviceSn(x.getDeviceSn());
                req.setCmd(cmd);
                boolean flag = iotDeviceService.controlKT(req);
                if (!flag) {
                    throw exception(DEVICE_OPRATION_ERROR);
                }
            });
        }
    }


    private void closeLight(Long roomId) {
        //1=门禁 2=空开 3=云喇叭 4=灯具 5=密码锁 6=网关 7=插座 8=锁球器控制器（12V） 9=人脸门禁机  10=智能语音喇叭 11=二维码识别器 12=红外控制器 13=三路控制器
        List<DeviceInfoDO> deviceList = deviceInfoMapper.getByRoomIdAndType(roomId, new Integer[]{4, 13});
        if (!CollectionUtils.isEmpty(deviceList)) {
            for (DeviceInfoDO x : deviceList) {
                switch (x.getType()) {
                    case 4:
                        opSwitch(x.getDeviceSn(), "off");
                        break;
                    case 13:
                        //三路控制器  只关第二路 第二路是灯
                        IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
                        List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
                        param.add(new IotDeviceContrlReqVO().setOutlet(1).setCmd("off"));
                        reqVO.setDeviceSn(x.getDeviceSn()).setParams(param);
                        boolean flag = iotDeviceService.control(reqVO);
                        if (!flag) {
                            throw exception(DEVICE_OPRATION_ERROR);
                        }
                        break;
                }

            }
        }
    }

}
