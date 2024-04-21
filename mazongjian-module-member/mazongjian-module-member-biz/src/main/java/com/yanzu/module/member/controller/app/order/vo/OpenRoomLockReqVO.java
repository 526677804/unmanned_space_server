package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OpenRoomLockReqVO {

    @Schema(description = "订单key")
    @NotNull(message = "参数错误")
    private String orderKey;

}
