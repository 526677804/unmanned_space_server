package com.yanzu.module.member.controller.admin.clearinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 保洁信息管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClearInfoRespVO extends ClearInfoBaseVO {

    @Schema(description = "清洁记录id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13416")
    private Long clearId;

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "30333")
    private Long orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "清洁图片")
    private String imgs;

    @Schema(description = "用户id", example = "23617")
    private Long userId;

    @Schema(description = "投诉的照片")
    private String complaintImgs;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "结算时间")
    private LocalDateTime settlementTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
