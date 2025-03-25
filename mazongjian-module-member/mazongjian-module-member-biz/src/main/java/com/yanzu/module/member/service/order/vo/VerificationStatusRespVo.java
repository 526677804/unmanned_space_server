package com.yanzu.module.member.service.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VerificationStatusRespVo {

     @Schema(description = "1.未核销 2.已核销 3.未知状态")
    private Integer consumeStatus;

     @Schema(description = "订单id")
    private String orderId;

}
