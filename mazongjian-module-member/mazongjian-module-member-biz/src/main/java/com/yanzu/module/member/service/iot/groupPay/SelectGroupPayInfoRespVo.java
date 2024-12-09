package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SelectGroupPayInfoRespVo {

    @Schema(name = "商家实际到账价格，单位：分 （计算规则：用户支付+平台优惠-商家优惠）")
    private Integer payAmount;

    @Schema(name = "团购券对应的商品 ID")
    private String shopId;

    @Schema(name = "团购券的标题、名称")
    private String ticketName;

    @Schema(name = "团购券的信息，加密数据。")
    private String ticketInfo;

    @Schema(name = "团购券的数据，团购平台完整返回过来的信息。")
    private String ticketData;

}
