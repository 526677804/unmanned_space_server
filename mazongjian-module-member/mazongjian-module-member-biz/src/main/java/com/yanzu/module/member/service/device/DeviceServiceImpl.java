package com.yanzu.module.member.service.device;

import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.deviceuseinfo.DeviceUseInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.mqtt.MqttProviderConfig;
import com.yanzu.module.member.service.iot.EwlService;
import com.yanzu.module.member.service.iot.IotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.UUID;

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
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Autowired
    private MqttProviderConfig mqttProvider;
    @Resource
    private IotService iotService;

    @Resource
    private EwlService ewlService;

    @Override
    @Transactional
    public void openStoreDoor(Long userId,Long storeId, int type) {
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


    private void openDoor(String sn) {
        if (!ObjectUtils.isEmpty(sn)) {
            boolean flag;
            //判断硬件平台类型 W开头是微门禁 其他则是易微联
            if (sn.startsWith("W")) {
                if (sn.startsWith("W89")) {
                    flag = iotService.runDoorV2(sn);
                } else {
                    flag = iotService.runDoorV1(sn);
                }
            } else {
                flag = ewlService.runKongkai(sn, "on");
            }
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }


    private void openRoomDoor(Long roomId) {
        //获取房间空开设备的sn
        String sn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 2);
        if (!ObjectUtils.isEmpty(sn)) {
            boolean flag;
            //判断硬件平台类型 W开头是微门禁 其他则是易微联
            if (sn.startsWith("W")) {
                flag = iotService.runKongkai(sn, "turnon");

            } else {
                flag = ewlService.runKongkai(sn, "on");
            }
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
        //可能有门禁  获取一下门禁
        String doorSn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 1);
        openDoor(doorSn);
    }

    private void closeRoomDoorV2(Long roomId) {
        //获取房间空开设备的sn
        String kongKaiSN = deviceInfoMapper.getSnByRoomIdAndType(roomId, 2);
        if (!ObjectUtils.isEmpty(kongKaiSN)) {
            boolean flag;
            //判断硬件平台类型 W开头是微门禁 其他则是易微联
            if (kongKaiSN.startsWith("W")) {
                flag = iotService.runKongkai(kongKaiSN, "turnoff");

            } else {
                flag = ewlService.runKongkai(kongKaiSN, "off");
            }
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }


    @Override
    @Transactional
    public void openRoomDoor(Long userId,Long storeId,Long roomId, int type) {
        if(!ObjectUtils.isEmpty(roomId)&&ObjectUtils.isEmpty(storeId)){
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
            storeId=roomInfoDO.getStoreId();
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
    public void closeRoomDoor(Long userId,Long storeId,Long roomId, int type) {
        if(!ObjectUtils.isEmpty(roomId)&&ObjectUtils.isEmpty(storeId)){
            RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);
            storeId=roomInfoDO.getStoreId();
        }
        //1用户关门 2管理员关门 3保洁关门  4系统关门
        switch (type) {
            case 1://1用户关门
                closeRoomDoorV2(roomId);
                break;
            case 2:
            case 3:
                //管理员 保洁也不限制关门
                closeRoomDoorV2(roomId);
                break;
            case 4:
                closeRoomDoorV2(roomId);
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
            if (sn.startsWith("MZJ")) {
                JSONObject data = new JSONObject();
                data.put("playAudibleMsg", "00" + type);
                data.put("orderId", UUID.randomUUID().toString());
                mqttProvider.publish(2, false, "yunlaba/" + sn, data.toJSONString());
            } else {
                //自有设备
                String str = "";
                switch (type) {
                    case 1:
                        str = "欢迎您光临,本店无人值守,需要帮助请联系客服，请您文明娱乐,禁止从事赌博等违法行为.祝您玩的开心！";
                        break;
                    case 2:
                        str = "您的订单剩余时间已不足三十分钟,到期后将自动关闭房间电源,请您及时进行续费,避免影响使用！";
                        break;
                    case 3:
                        str = "您的订单剩余时间已不足十五分钟,到期后将自动关闭房间电源,请您及时进行续费,避免影响使用！";
                        break;
                    case 4:
                        str = "您的订单剩余时间已不足五分钟,到期后将自动关闭房间电源,请您及时进行续费,避免影响使用！";
                        break;
                    case 5:
                        str = "尊敬的顾客您好,根据城市管理条例要求,请您在深夜消费时,注意控制噪音,以免影响到他人,感谢您的支持与理解！";
                        break;
                }
                boolean flag = iotService.runYunlaba(sn, str, roomInfoDO.getYunlabaSound());
                if (!flag) {
                    throw exception(DEVICE_OPRATION_ERROR);
                }
            }
        }
    }

    @Override
    @Transactional
    public void weimenjin(JSONObject body) {
        //收到智能硬件回调:{"device_sn":"W71F9783B28","cmd":"notify","msg_id":0,"type":2,"app_id":"","cmd_type":"notify","info":{"notify_type":"on-off","state":1}}
        //收到智能硬件回调:{"device_sn":"W71F9783B28","cmd":"notify","msg_id":0,"type":2,"app_id":"","cmd_type":"notify","info":{"notify_type":"on-off","state":0}}
        String device_sn = body.getString("device_sn");
        if (body.getString("cmd").equals("notify")) {
            JSONObject info = body.getJSONObject("info");
            Integer state = info.getInteger("state");
            if (!ObjectUtils.isEmpty(state)) {
                if (state == 1) {
                    //上线
                    log.info("智能硬件，上线，设备:{}", device_sn);
                    deviceInfoMapper.updateStatusBySN(device_sn, 1);
                } else {
                    //下线
                    log.info("智能硬件，离线，设备:{}", device_sn);
                    deviceInfoMapper.updateStatusBySN(device_sn, 0);
                }
            }
        }


        //收到智能硬件回调:{"device_sn":"W70F9783D78","cmd":"OnLine","msg_id":0,"type":0,"app_id":"","cmd_type":"OnLine","info":{"time":1694930336}}

        //收到智能硬件回调:{"device_sn":"W70F9783D78","cmd":"dev_reg","msg_id":0,"type":2,"app_id":"","cmd_type":"dev_reg","info":{"hw_ver":"1.0.0","iccid":"535479429655B2D5","imei":"6055F9783D78","project":"WMJ_CLOUDSPEAKER_C3","rssi":-52,"sw_ver":"1.0.7","username":"W70F9783D78"}}
        //{"device_sn":"W70F9783D78","cmd":"dev_reg","msg_id":0,"type":2,"app_id":"","cmd_type":"dev_reg","info":{"hw_ver":"1.0.0","iccid":"535479429655B2D5","imei":"6055F9783D78","project":"WMJ_CLOUDSPEAKER_C3","rssi":-52,"sw_ver":"1.0.7","username":"W70F9783D78"}}


    }

    @Override
    public void testYunlaba(Long roomId) {
        runSound(roomId, 1);
    }
}
