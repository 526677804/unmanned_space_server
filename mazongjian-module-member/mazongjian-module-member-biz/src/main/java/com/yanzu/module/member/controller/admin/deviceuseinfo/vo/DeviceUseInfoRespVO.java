package com.yanzu.module.member.controller.admin.deviceuseinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备使用记录 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceUseInfoRespVO extends DeviceUseInfoBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6466")
    private Long id;

    @Schema(description = "用户id", example = "10776")
    private Long userId;

    @Schema(description = "用户昵称", example = "10776")
    private String nickname;

    @Schema(description = "用户手机号", example = "10776")
    private String mobile;

    @Schema(description = "门店id", example = "30308")
    private Long storeId;

    @Schema(description = "房间id", example = "14482")
    private Long roomId;

    @Schema(description = "门店", example = "30308")
    private String storeName;

    @Schema(description = "房间", example = "14482")
    private String roomName;

    @Schema(description = "命令")
    private String cmd;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
