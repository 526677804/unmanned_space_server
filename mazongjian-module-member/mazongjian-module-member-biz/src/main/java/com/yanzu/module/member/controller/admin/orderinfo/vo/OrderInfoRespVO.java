package com.yanzu.module.member.controller.admin.orderinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 订单管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderInfoRespVO extends OrderInfoBaseVO {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2248")
    private Long orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2319")
    private Long roomId;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4765")
    private Long userId;

    @Schema(description = "订单开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @Schema(description = "订单结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    @Schema(description = "订单价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "1698")
    private BigDecimal price;

    @Schema(description = "工作日折扣", example = "2948")
    private Integer workDiscount;

    @Schema(description = "实际支付价格", example = "6888")
    private BigDecimal payPrice;

    @Schema(description = "退款价格", example = "8062")
    private BigDecimal refundPrice;

    @Schema(description = "支付方式", example = "1")
    private Integer payType;

    @Schema(description = "团购券码")
    private String groupPayNo;

    @Schema(description = "优惠券Id", example = "31071")
    private Long couponId;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
