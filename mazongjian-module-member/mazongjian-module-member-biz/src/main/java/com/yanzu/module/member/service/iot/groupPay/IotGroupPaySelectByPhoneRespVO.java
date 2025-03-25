package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IotGroupPaySelectByPhoneRespVO {

     @Schema(description = "团购券码")
    private String ticketNo;

     @Schema(description = "团购券名称、标题")
    private String ticketName;

     @Schema(description = "团购券对应的商品 ID,团购平台返回的")
    private String shopId;


}
