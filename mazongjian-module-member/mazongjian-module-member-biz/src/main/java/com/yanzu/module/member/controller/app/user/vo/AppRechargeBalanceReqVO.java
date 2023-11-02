package com.yanzu.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

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

    @Schema(description = "用户Id", example = "1")
    @NotNull(message = "用户Id不能为空")
    private Long userId;


    @Schema(description = "门店Id 必传", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "充值金额,单位为分 必传", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "充值金额不能为空")
    private Integer price;


    @Schema(description = "微信支付订单编号 必传", requiredMode = Schema.RequiredMode.REQUIRED, example = "20231123123123123123")
    @NotNull(message = "orderNo不能为空")
    private String orderNo;


}
