package com.yanzu.module.member.service.device;

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
                break;
            case 2://2管理员开门
            case 3://3保洁开门
                //这里是开门店的大门，所以随便开
                openStoreDoorV1(storeId);
                break;
        }
    }

    private void openStoreDoorV1(Long storeId) {
        //获取大门的门禁sn
        String sn = deviceInfoMapper.getDateSnByStoreId(storeId);
        if (!ObjectUtils.isEmpty(sn)) {
            boolean flag = iotService.runDoorV1(sn);
            if (!flag) {
                throw exception(DEVICE_OPRATION_ERROR);
            }
        }
    }

    /**
     *
     * @param storeId
     * @param orderId
     * @param type 1用户开门 2管理员开门 3保洁开门
     */
    @Override
    @Transactional
    public void cloudStoreDoor(Long storeId, Long orderId, int type) {
        //1用户开门 2管理员开门 3保洁开门

    }

    @Override
    @Transactional
    public void openRoomDoor(Long roomId, Long orderId, int type) {
        //1用户开门 2管理员开门 3保洁开门

    }

    @Override
    @Transactional
    public void closeRoomDoor(Long roomId, Long orderId, int type) {
        //1用户开门 2管理员开门 3保洁开门

    }
}
