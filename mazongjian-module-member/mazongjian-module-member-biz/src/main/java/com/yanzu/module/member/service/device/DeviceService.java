package com.yanzu.module.member.service.device;

import com.alibaba.fastjson.JSONObject;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.device
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/28 12:12
 */
public interface DeviceService {

    void openStoreDoor(Long storeId, Long orderId, int type);

    void openRoomDoor(Long roomId, Long orderId, int type);

    void closeRoomDoor(Long roomId, Long orderId, int type);

    /**
     * @param roomId 房间id
     * @param type   提示语类型 1欢迎语 2结束时间30分钟提醒  3结束时间15分钟提示  4 结束时间5分钟提醒
     */
    void runSound(Long roomId, Integer type);

    void weimenjin(JSONObject body);
}
