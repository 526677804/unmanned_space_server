package com.yanzu.module.member.service.iot.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IotAddLockRespVO {
    @Schema(description = "设备编号")
    private String deviceSn;


    private Boolean success;
}
