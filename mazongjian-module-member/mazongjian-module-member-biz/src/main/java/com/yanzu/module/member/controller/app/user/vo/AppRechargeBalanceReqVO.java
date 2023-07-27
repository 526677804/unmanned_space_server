package com.yanzu.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.user.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 16:00
 */
@Schema(description = "miniapp - 用户余额充值 Request VO")
@Data
@ToString(callSuper = true)
public class AppRechargeBalanceReqVO {

    @Schema(description = "用户Id 管理员代为充值的时候才传", example = "1")
    private Long userId;


    @Schema(description = "门店Id 必传", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "门店不能为空")
    private Long storeId;


    @Schema(description = "支付订单Id 必传", requiredMode = Schema.RequiredMode.REQUIRED, example = "20231123123123123123")
    @NotEmpty(message = "支付id不能为空")
    private String payOrderId;


}
