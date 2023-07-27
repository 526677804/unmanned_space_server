package com.yanzu.module.member.controller.admin.deviceuseinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备使用记录 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceUseInfoRespVO extends DeviceUseInfoBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1434")
    private Long id;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3090")
    private Long userId;

    @Schema(description = "设备id", example = "27533")
    private Long deviceId;

    @Schema(description = "设备sn")
    private String deviceNo;

    @Schema(description = "命令")
    private String cmd;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
