package com.yanzu.module.member.service.iot.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IotDeviceDelBlacklistReqVO {

    @NotNull(message = "人员ID不能为空")
    @Schema(description = "人员ID")
    private String admitGuid;

    @Schema(description = "门店ID")
    @NotNull(message = "门店ID不能为空")
    private Long storeId;

}
