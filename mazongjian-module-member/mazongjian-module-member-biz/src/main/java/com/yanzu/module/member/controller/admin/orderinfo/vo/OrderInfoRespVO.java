package com.yanzu.module.member.controller.admin.orderinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 订单管理 Response VO")
@Data
@ToString(callSuper = true)
public class OrderInfoRespVO {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "18239")
    private Long orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "门店名称", example = "2278")
    private String storeName;

    @Schema(description = "房间id", example = "22110")
    private Long roomId;

    @Schema(description = "房间名称", example = "22110")
    private String roomName;

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2278")
    private Long storeId;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14603")
    private Long userId;

    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "14603")
    private String nickname;

    @Schema(description = "订单开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    @Schema(description = "订单结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    @Schema(description = "订单价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "20429")
    private BigDecimal price;

    @Schema(description = "押金", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal deposit;

    @Schema(description = "实际支付价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "24753")
    private BigDecimal payPrice;

    @Schema(description = "退款价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "18414")
    private BigDecimal refundPrice;

    @Schema(description = "支付方式", example = "1")
    private Integer payType;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
