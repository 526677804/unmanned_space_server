package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class IotGroupPayGetYDCancelAuthListRespVO {


    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "开始时间")
    private String beginTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "订单实际支付价格")
    private Integer amount;

    @Schema(description = "核销状态：1-默认，2-核销成功，3-核销失败")
    private Integer verificationStatus;

    @Schema(description = "预订发起时间")
    private String bookTime;


}
