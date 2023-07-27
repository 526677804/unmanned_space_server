package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceInfoRespVO extends DeviceInfoBaseVO {

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "28091")
    private Long deviceId;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
