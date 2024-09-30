package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OpenRoomLockReqVO {

    @Schema(description = "订单key")
    @NotNull(message = "参数错误")
    private String orderKey;

    @Schema(description = "开关机")
    private Boolean power;

    @Schema(description = "温度")
    @Min(value = 16, message = "温度最小16")
    @Max(value = 30, message = "温度最大30")
    private Integer temperature;

    @Schema(description = "模式 cool=制冷 heat=制热 dehumidify=除湿 auto=自动")
    private String mode;

    @Schema(description = "上下扫风")
    private Boolean verticalSwing;

    @Schema(description = "左右扫风")
    private Boolean horizontalSwing;

    @Schema(description = "风速调节 0=减小 1=增大")
    private Integer fanSpeed;

}
