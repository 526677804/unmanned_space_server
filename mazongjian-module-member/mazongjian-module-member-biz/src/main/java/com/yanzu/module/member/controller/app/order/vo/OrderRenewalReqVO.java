package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 15:25
 */
@Schema(description = "miniapp - 订单续费Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRenewalReqVO {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long orderId;

    @Schema(description = "续费时长  以分钟为单位，0.5小时=30分钟", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer minutes;

    @Schema(description = "支付方式 值见字典", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "支付方式不能为空")
    private Integer payType;

    @Schema(description = "支付订单Id 微信支付时传", requiredMode = Schema.RequiredMode.REQUIRED, example = "20231123123123123123")

    private String payOrderId;

}
