package com.yanzu.module.member.controller.admin.userwithdrawal.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户提现 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserWithdrawalRespVO extends UserWithdrawalBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3450")
    private Long id;

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String no;

    @Schema(description = "用户Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "145")
    private Long userId;

    @Schema(description = "提现金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal money;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Boolean status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
