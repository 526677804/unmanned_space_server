package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeviceControlReqVO {

    @Schema(description = "设备id")
    @NotNull(message = "设备不能为空")
    private Long deviceId;


    @Schema(description = "命令")
    @NotNull(message = "命令不能为空")
    private String cmd;


}
