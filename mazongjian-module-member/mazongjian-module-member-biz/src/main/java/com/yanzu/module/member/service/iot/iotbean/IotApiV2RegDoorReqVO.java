package com.yanzu.module.member.service.iot.iotbean;

import lombok.Data;

@Data
public class IotApiV2RegDoorReqVO {

    private String cmd_type = "active";

    private IotApiV2RegDoorDataReqVO data;


}
