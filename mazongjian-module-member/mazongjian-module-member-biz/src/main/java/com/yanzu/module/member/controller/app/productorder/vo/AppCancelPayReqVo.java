package com.yanzu.module.member.controller.app.productorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppCancelPayReqVo {

    @NotNull(message = "订单不能为空")
    @Schema(description = "订单id")
    private Long orderId;

}
