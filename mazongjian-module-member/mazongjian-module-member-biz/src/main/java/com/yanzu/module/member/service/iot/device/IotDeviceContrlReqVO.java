package com.yanzu.module.member.service.iot.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @PACKAGE_NAME: cn.houey.module.system.controller.admin.iot.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2024/3/27 15:47
 */
@Data
public class IotDeviceContrlReqVO{

    @Schema(description = "通道 设备可能有多个通道,从0开始")
    @NotNull
    private Integer outlet;

    @Schema(description = "命令 开关传 on或off 音箱传声音编号,从1开始")
    @NotNull
    private String cmd;

    @Schema(description = "命令值")
    private Integer type;


}
