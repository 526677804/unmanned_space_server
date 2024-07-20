package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IotGroupPayPrepareRespVO {

    @Schema(description = "用户实际支付价格  单位 分")
    private Integer payAmount;

    @Schema(description = "自定义商品ID")
    private String shopId;

    @Schema(description = "团购券名称")
    private String ticketName;

    @Schema(description = "团购券信息")
    private String ticketInfo;

    @Schema(description = "原始团购券数据")
    private String ticketData;

    @Schema(description = "团购平台类型")
    private Integer groupPayType;

}
