package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectGroupPayInfoReqVo {

     @Schema(description = "门店id")
    private Long storeId;

     @Schema(description = "团购券码")
    private String ticketNo;

     @Schema(description = "团购平台类型")
    private String groupPayType;

}
