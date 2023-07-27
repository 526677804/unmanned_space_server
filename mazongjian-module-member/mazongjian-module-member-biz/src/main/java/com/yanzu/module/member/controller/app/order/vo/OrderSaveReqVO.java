package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 18:25
 */
@Schema(description = "miniapp - 提交订单Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSaveReqVO {

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2319")
    private Long roomId;

    @Schema(description = "订单开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @Schema(description = "订单结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    @Schema(description = "支付方式 值见枚举", example = "1")
    private Integer payType;

    @Schema(description = "团购券码 填了团购券时，其他支付方式均不生效")
    private String groupPayNo;

    @Schema(description = "优惠券Id", example = "31071")
    private Long couponId;


}
