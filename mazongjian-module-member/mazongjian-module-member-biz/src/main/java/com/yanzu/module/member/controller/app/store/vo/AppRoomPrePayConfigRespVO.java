package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AppRoomPrePayConfigRespVO {

    @Schema(description = "房间Id")
    @NotNull(message = "房间不能为空")
    private Long roomId;

    @Schema(description = "预付费价格")
    @NotNull(message = "预付费价格不能为空")
    @Min(value = 1,message = "预付费价格不能小于1")
    private BigDecimal prePrice;

    @Schema(description = "预付费计价分钟")
    @NotNull(message = "计费单位不能为空")
    @Min(value = 1,message = "计费单位不能小于1")
    @Max(value = 60,message = "计费单位不能大于60")
    private Integer preUnit;

    @Schema(description = "最低消费价格")
    private BigDecimal minCharge;


}
