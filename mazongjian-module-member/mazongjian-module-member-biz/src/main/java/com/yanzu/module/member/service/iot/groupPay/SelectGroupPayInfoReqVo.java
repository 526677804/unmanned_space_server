package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectGroupPayInfoReqVo {

    @Schema(name = "门店id")
    private Long storeId;

    @Schema(name = "团购券码")
    private String ticketNo;

    @Schema(name = "团购平台类型")
    private String groupPayType;

}
