package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/8/28 17:04
 */
@Schema(description = "miniapp - 获取门店充值优惠规则 Resp VO")
@Data
@ToString(callSuper = true)
public class AppDiscountRulesRespVO {
    @Schema(description = "支付金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal payMoney;

    @Schema(description = "赠送金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal giftMoney;
}
