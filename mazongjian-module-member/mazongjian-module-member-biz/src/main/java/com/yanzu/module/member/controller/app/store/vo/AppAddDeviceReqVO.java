package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.validation.constraints.NotNull;

@Data
public class AppAddDeviceReqVO {

    @Schema(description = "设备编号")
    @NotNull(message = "设备编号不能为空")
    private String deviceSn;

    @Schema(description = "设备类型")
    @NotNull(message = "设备类型不能为空")
    private Integer deviceType;

    @Schema(description = "门店id")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "房间id")
    private Long roomId;

    @Schema(description = "是否共用设备")
    private Boolean shareDevice;


}
