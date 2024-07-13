package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IotGroupPayPrepareReqVO {


    @Schema(description = "门店id")
    @NotNull(message = "门店id不能为空")
    private Long storeId;


    @Schema(description = "团购券码")
    @NotNull(message = "团购券码不能为空")
    private String ticketNo;


    @Schema(description = "团购平台类型")
    private Integer groupPayType;

}
