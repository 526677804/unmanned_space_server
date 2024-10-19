package com.yanzu.module.member.service.iot.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class IotControlKTReqVO {

    @Schema(description = "设备编号")
    @NotNull(message = "设备编号不能为空")
    private String deviceSn;


    @Schema(description = "控制命令")
    @NotNull(message = "命令不能为空")
    private String cmd;





}
