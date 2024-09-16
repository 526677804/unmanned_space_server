package com.yanzu.module.member.service.iot.device;

import lombok.Data;

@Data
public class IotDeviceRoomInfoVO {

    private Long deviceId;

    private String deviceSn;

    private Long roomId;

    private String roomName;

    private Long storeId;

    private Integer type;

    private Long tenantId;


}
