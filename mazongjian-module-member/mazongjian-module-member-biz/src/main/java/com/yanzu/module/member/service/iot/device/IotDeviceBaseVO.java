package com.yanzu.module.member.service.iot.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @PACKAGE_NAME: cn.houey.module.system.controller.admin.iot.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2024/3/27 15:43
 */
@Data
public class IotDeviceBaseVO<T> {


    @Schema(description = "设备sn号")
    @NotNull(message = "deviceSn不能为空")
    private String deviceSn;


    @Schema(description = "ts,当前时间戳")
    @NotNull(message = "ts不能为空")
    private Long ts;

    @Schema(description = "参数")
//    @NotNull(message = "params不能为空")
    private List<T> params;


}
