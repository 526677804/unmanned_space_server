package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 设备管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceInfoCreateReqVO extends DeviceInfoBaseVO {

    @Schema(description = "设备sn", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "设备sn不能为空")
    private String deviceSn;

    @Schema(description = "设备类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "设备类型不能为空")
    private Integer type;

    @Schema(description = "门店id")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "房间id")
    private Long roomId;

    @Schema(description = "是否多房间共享设备")
    private Boolean share;

}
