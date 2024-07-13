package com.yanzu.module.member.service.iot.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IotDeviceSetAutoLockReqVO extends IotDeviceBaseVO<IotDeviceSetAutoLockReqVO> {

    @Schema(description = "自动关锁时间")
    @NotNull(message = "自动关锁时间不能为空")
    private Integer secend;

}