package com.yanzu.module.member.service.device;

import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;
import com.yanzu.module.member.dal.mysql.deviceuseinfo.DeviceUseInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.service.iot.IotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.device
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/28 12:13
 */
@Service
@Validated
public class DeviceServiceImpl implements DeviceService {


    @Resource
    private DeviceUseInfoMapper deviceUseInfoMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;


    @Resource
    private IotService iotService;

    @Override
    @Transactional
    public void openStoreDoor(Long storeId, Long orderId, int type) {
        //1用户开门 2管理员开门 3保洁开门
        switch (type) {
            case 1://1用户开门
                if (ObjectUtils.isEmpty(orderId)) {
                    //没有传订单
                    //找出用户进行中的订单
                    OrderInfoDO orderInfoDO = orderInfoMapper.getByUserAndStatus(getLoginUserId(), AppEnum.order_status.START.getValue());
                    if (ObjectUtils.isEmpty(orderInfoDO)) {
                        throw exception(NOT_START_ORDER);
                    } else {
                        openStoreDoorV1(orderInfoDO.getStoreId());
                    }
                } else {
                    //从订单开门的
                    OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
                    if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                        openStoreDoorV1(orderInfoDO.getStoreId());
                    } else if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
                        throw exception(ORDER_STATUS_NOT_START_ERROR);
                    }
                }
                //增加开门记录
                saveDeviceUseRecord(getLoginUserId(), storeId, null, "openStoreDoor");
                break;
            case 2://2管理员开门
            case 3://3保洁开门
                //这里是开门店的大门，所以随便开
                openStoreDoorV1(storeId);
                //增加开门记录
                saveDeviceUseRecord(getLoginUserId(), storeId, null, "openStoreDoor");
                break;
            case 4:
                //增加开门记录
                saveDeviceUseRecord(null, storeId, null, "openStoreDoor");
                break;
        }

    }


    private void saveDeviceUseRecord(Long userId, Long storeId, Long roomId, String cmd) {
        DeviceUseInfoDO deviceUseInfoDO = new DeviceUseInfoDO();
        deviceUseInfoDO.setUserId(userId);
        deviceUseInfoDO.setStoreId(storeId);
        deviceUseInfoDO.setRoomId(roomId);
        deviceUseInfoDO.setCmd(cmd);
        deviceUseInfoMapper.insert(deviceUseInfoDO);
    }

    private void openStoreDoorV1(Long storeId) {
        //获取大门的门禁sn
        String sn = deviceInfoMapper.getSnByStoreId(storeId);
        if (!ObjectUtils.isEmpty(sn)) {
            boolean flag = iotService.runDoorV1(sn);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }

    private void openRoomDoorV2(Long roomId) {
        //获取设备的门禁sn
        String sn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 2);
        if (!ObjectUtils.isEmpty(sn)) {
            boolean flag = iotService.runKongkai(sn, "turnon");
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }

    private void clouseRoomDoorV2(Long roomId) {
        //获取设备的门禁sn
        String sn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 2);
        if (!ObjectUtils.isEmpty(sn)) {
            boolean flag = iotService.runKongkai(sn, "turnoff");
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }


    @Override
    @Transactional
    public void openRoomDoor(Long roomId, Long orderId, int type) {
        //1用户开门 2管理员开门 3保洁开门 4系统开门
        switch (type) {
            case 1://1用户开门
                if (ObjectUtils.isEmpty(orderId)) {
                    //没有传订单
                    //找出用户进行中的订单
                    OrderInfoDO orderInfoDO = orderInfoMapper.getByUserAndStatus(getLoginUserId(), AppEnum.order_status.START.getValue());
                    if (ObjectUtils.isEmpty(orderInfoDO)) {
                        throw exception(NOT_START_ORDER);
                    } else {
                        openRoomDoorV2(orderInfoDO.getRoomId());
                    }
                } else {
                    //从订单开门的
                    OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
                    if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                        openRoomDoorV2(orderInfoDO.getRoomId());
                    } else if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
                        throw exception(ORDER_STATUS_NOT_START_ERROR);
                    }
                }
                //增加开门记录
                saveDeviceUseRecord(getLoginUserId(), null, roomId, "openRoomDoor");
                break;
            case 2:
            case 3:
                //管理员不限制 保洁开门权限在调用处控制 只能开待清洁的房间
                openRoomDoorV2(roomId);
                //增加开门记录
                saveDeviceUseRecord(getLoginUserId(), null, roomId, "openRoomDoor");
                break;
            case 4:
                openRoomDoorV2(roomId);
                //增加开门记录
                saveDeviceUseRecord(null, null, roomId, "openRoomDoor");
                break;
        }
    }

    @Override
    @Transactional
    public void closeRoomDoor(Long roomId, Long orderId, int type) {
        //1用户关门 2管理员关门 3保洁关门  4系统关门
        switch (type) {
            case 1://1用户关门
                if (ObjectUtils.isEmpty(orderId)) {
                    //没有传订单
                    //找出用户进行中的订单
                    OrderInfoDO orderInfoDO = orderInfoMapper.getByUserAndStatus(getLoginUserId(), AppEnum.order_status.START.getValue());
                    if (ObjectUtils.isEmpty(orderInfoDO)) {
                        throw exception(NOT_START_ORDER);
                    } else {
                        clouseRoomDoorV2(orderInfoDO.getRoomId());
                    }
                } else {
                    //从订单关门的
                    OrderInfoDO orderInfoDO = orderInfoMapper.selectById(orderId);
                    if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                        clouseRoomDoorV2(orderInfoDO.getRoomId());
                    } else if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0) {
                        throw exception(ORDER_STATUS_NOT_START_ERROR);
                    }
                }
                //增加记录
                saveDeviceUseRecord(getLoginUserId(), null, roomId, "closeRoomDoor");
                break;
            case 2:
            case 3:
                //管理员 保洁也不限制关门
                clouseRoomDoorV2(roomId);
                //增加记录
                saveDeviceUseRecord(getLoginUserId(), null, roomId, "closeRoomDoor");
                break;
            case 4:
                clouseRoomDoorV2(roomId);
                //增加记录
                saveDeviceUseRecord(null, null, roomId, "closeRoomDoor");
                break;
        }

    }

    //提示语类型 1欢迎语 2结束时间30分钟提醒  3结束时间15分钟提示  4 结束时间5分钟提醒
    @Override
    public void runSound(Long roomId, Integer type) {
        //获取设备的sn
        String sn = deviceInfoMapper.getSnByRoomIdAndType(roomId, 3);
        if (!ObjectUtils.isEmpty(sn)) {
            String str = "";
            switch (type) {
                case 1:
                    str = "欢迎您光临自助棋牌室,本店无人值守,需要帮助请电话或微信联系客服.请您文明娱乐,禁止从事赌博等违法行为.祝您玩的开心！";
                    break;
                case 2:
                    str = "您的订单剩余消费时间已不足三十分钟,到期后将自动关闭房间电源,请您及时进行续费,避免影响使用！";
                    break;
                case 3:
                    str = "您的订单剩余消费时间已不足十五分钟,到期后将自动关闭房间电源,请您及时进行续费,避免影响使用！";
                    break;
                case 4:
                    str = "您的订单剩余消费时间已不足五分钟,到期后将自动关闭房间电源,请您及时进行续费,避免影响使用！";
                    break;
            }
            boolean flag = iotService.runYunlaba(sn, str);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }
}
