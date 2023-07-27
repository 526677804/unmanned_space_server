package com.yanzu.module.member.controller.admin.gameinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 在线组局管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GameInfoRespVO extends GameInfoBaseVO {

    @Schema(description = "对局ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31151")
    private Long gameId;

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32343")
    private Long storeId;

    @Schema(description = "房间ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14759")
    private Long roomId;

    @Schema(description = "规则描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ruleDesc;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26195")
    private Long userId;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
