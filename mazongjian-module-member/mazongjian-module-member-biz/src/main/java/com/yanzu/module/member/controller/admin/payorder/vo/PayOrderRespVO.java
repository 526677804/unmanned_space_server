package com.yanzu.module.member.controller.admin.payorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 支付订单 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PayOrderRespVO extends PayOrderBaseVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18026")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "14712")
    private Long userId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "订单内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderDesc;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "30899")
    private Integer price;

    @Schema(description = "是否已支付", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Boolean payStatus;

    @Schema(description = "支付订单编号")
    private String payOrderNo;

    @Schema(description = "订单支付时间")
    private LocalDateTime payTime;

    @Schema(description = "退款订单编号")
    private String payRefundNo;

    @Schema(description = "退款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "22993")
    private Integer refundPrice;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
