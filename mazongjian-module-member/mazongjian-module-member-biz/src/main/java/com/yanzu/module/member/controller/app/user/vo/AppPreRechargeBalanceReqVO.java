package com.yanzu.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.user.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 16:00
 */
@Schema(description = "miniapp - 用户余额充值预下单 Request VO")
@Data
@ToString(callSuper = true)
public class AppPreRechargeBalanceReqVO {

    @Schema(description = "用户Id 管理员代为充值的时候才传", example = "1")
    private Long userId;


    @Schema(description = "门店Id 必传", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "充值金额,单位为分 必传", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "充值金额不能为空")
    @Min(value = 1, message = "充值金额不能小于0")
    @Max(value = 1000000, message = "充值金额不能大于10000")
    private Integer price;

}
