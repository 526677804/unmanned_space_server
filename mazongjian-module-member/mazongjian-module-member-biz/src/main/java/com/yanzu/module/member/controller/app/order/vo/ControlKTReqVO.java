package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ControlKTReqVO {

    @Schema(description = "订单key")
    @NotNull(message = "参数错误")
    private String orderKey;

    @Schema(description = "控制命令")
    @NotNull(message = "命令不能为空")
    private String cmd;

}
