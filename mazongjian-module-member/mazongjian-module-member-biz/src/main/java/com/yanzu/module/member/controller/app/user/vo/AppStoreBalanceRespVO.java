package com.yanzu.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Schema(description = "miniapp - 用户门店余额 Response VO")
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AppStoreBalanceRespVO {
    @Schema(description = "账户余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal balance;


    @Schema(description = "赠送余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal giftBalance;
}

