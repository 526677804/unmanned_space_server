package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 设备绑定 Request VO")
@Data
@ToString(callSuper = true)
public class DeviceInfoBindReqVO  {

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "设备id不能为空")
    private Long deviceId;

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "房间ID")
    private Long roomId;

}
