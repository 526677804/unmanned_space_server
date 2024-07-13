package com.yanzu.module.member.service.iot.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IotDeviceLockUpdateReqVO extends IotDeviceBaseVO<IotDeviceLockUpdateReqVO> {

    @Schema(description = "锁数据")
    @NotNull(message = "锁数据不能为空")
    private String lockData;
}