package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IotGroupPaySelectByPhoneRespVO {

    @Schema(name = "团购券码")
    private String ticketNo;

    @Schema(name = "团购券名称、标题")
    private String ticketName;

    @Schema(name = "团购券对应的商品 ID,团购平台返回的")
    private String shopId;


}
