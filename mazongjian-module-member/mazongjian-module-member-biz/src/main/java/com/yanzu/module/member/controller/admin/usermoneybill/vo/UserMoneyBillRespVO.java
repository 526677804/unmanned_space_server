package com.yanzu.module.member.controller.admin.usermoneybill.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户账单明细 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserMoneyBillRespVO extends UserMoneyBillBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25644")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10013")
    private Long userId;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal money;

    @Schema(description = "金额类型", example = "1")
    private Integer moneyType;

    @Schema(description = "当时总账户余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalMoney;

    @Schema(description = "当时总赠送余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalGiftMoney;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
