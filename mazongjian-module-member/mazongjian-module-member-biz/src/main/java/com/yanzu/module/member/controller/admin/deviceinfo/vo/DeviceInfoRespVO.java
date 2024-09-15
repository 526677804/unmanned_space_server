package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceInfoRespVO extends DeviceInfoBaseVO {

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "9449")
    private Long deviceId;

    @Schema(description = "设备sn", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deviceSn;

    @Schema(description = "设备类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "是否多房间共享设备")
    private Boolean share;

    @Schema(description = "门店id", example = "8462")
    private Long storeId;

    @Schema(description = "门店名称", example = "8462")
    private String storeName;

    @Schema(description = "房间id", example = "2367")
    private Long roomId;

    @Schema(description = "房间名称", example = "2367")
    private String roomName;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "信号强度")
    private Integer rssi;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
