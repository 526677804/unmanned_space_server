package com.yanzu.module.member.service.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StartBookingRespVo {

    @Schema(name = "订单ID")
    private String orderId;

    @Schema(name = "第三方的订单号")
    private String appOrderId;

    @Schema(name = "预订手机号")
    private String mobile;

}
