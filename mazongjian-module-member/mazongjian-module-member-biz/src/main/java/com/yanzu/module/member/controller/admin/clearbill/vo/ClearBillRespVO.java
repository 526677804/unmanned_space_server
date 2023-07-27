package com.yanzu.module.member.controller.admin.clearbill.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 保洁账单管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClearBillRespVO extends ClearBillBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24862")
    private Long id;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "24116")
    private Long userId;

    @Schema(description = "结算金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal money;

    @Schema(description = "订单数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer orderNum;

    @Schema(description = "订单ids", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderIds;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
