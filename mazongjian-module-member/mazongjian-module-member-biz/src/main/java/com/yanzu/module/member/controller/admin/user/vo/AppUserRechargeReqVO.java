package com.yanzu.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AppUserRechargeReqVO {


    @Schema(description = "用户id")
    @NotNull(message = "用户不能为空")
    private Long userId;

    @Schema(description = "门店id")
    @NotNull(message = "充值门店不能为空")
    private Long storeId;

    @Schema(description = "充值金额")
    @NotNull(message = "充值金额不能为空")
    private BigDecimal money;


}
