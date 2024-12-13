package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IotGroupPayAuditYDReqVO {

    @Schema(description = "订单id")
    @NotNull(message = "订单不能为空")
    private String orderId;

    @Schema(description = "门店id")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "审核结果  2-审核通过，3-审核未通过")
    @NotNull(message = "审核结果不能为空")
    //审核结果    2-审核通过，3-审核未通过
    private Integer auditResult;


}
