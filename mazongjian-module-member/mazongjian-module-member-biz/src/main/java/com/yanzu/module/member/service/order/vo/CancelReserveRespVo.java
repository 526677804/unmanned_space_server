package com.yanzu.module.member.service.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CancelReserveRespVo {

    @Schema(name = "订单id")
    private String orderId;

}
