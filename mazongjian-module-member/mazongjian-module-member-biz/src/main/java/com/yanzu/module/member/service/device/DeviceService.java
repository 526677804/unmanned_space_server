package com.yanzu.module.member.service.device;

import com.yanzu.module.member.controller.app.order.vo.ControlKTReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppAddDeviceReqVO;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.device
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/28 12:12
 */
public interface DeviceService {

    void openStoreDoor(Long userId,Long storeId, int type);

    void openRoomDoor(Long userId,Long storeId,Long roomId, int type);

    void openRoomBlueLock(Long userId,Long storeId,Long roomId, int type);

    void closeRoomDoor(Long userId,Long storeId,Long roomId, int type);

    /**
     * @param roomId 房间id
     * @param type   提示语类型 1欢迎语 2结束时间30分钟提醒  3结束时间15分钟提示  4 结束时间5分钟提醒
     */
    void runSound(Long roomId, Integer type);

    void testYunlaba(Long roomId);

    void closeLightByRoomId(Long userId,Long storeId,Long roomId, int type);

    void clearByRoomId(Long roomId);

    int countGateway(Long storeId);

    int countKongtiao(Long roomId);

    String addUserFace(Long storeId, String photoUrl, String remark);

    void delUserFace(Long storeId, String admitGuid);

    void controlKT(String cmd, Long storeId,Long roomId);
}
