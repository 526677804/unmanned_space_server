package com.yanzu.module.member.service.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StartBookingRespVo {

     @Schema(description = "订单ID")
    private String orderId;

     @Schema(description = "第三方的订单号")
    private String appOrderId;

     @Schema(description = "预订手机号")
    private String mobile;

}
