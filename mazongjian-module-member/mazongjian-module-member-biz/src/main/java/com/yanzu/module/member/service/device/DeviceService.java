package com.yanzu.module.member.service.device;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.device
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/28 12:12
 */
public interface DeviceService {

    void openStoreDoor(Long storeId, Long orderId, int type);

    void cloudStoreDoor(Long storeId, Long orderId, int type);

    void openRoomDoor(Long roomId, Long orderId, int type);

    void closeRoomDoor(Long roomId, Long orderId, int type);

}
