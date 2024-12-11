package com.yanzu.module.member.controller.app.reserve.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StoreRulePushReqVo {

    @Schema(name = "门店Id")
    private Integer storeId;

    @Schema(name = "是否可退，0-可退，1-不可退")
    private Integer refundable;

    @Schema(name = "提前可订时间，分钟数，如：90，表示90分钟")
    private Integer addOrderBefore;

    @Schema(name = "提前可退时间，在refundable为不可退时，该字段可以不传，传了也是无效的 分钟数，如：90，表示90分钟")
    private Integer refundBefore;

    @Schema(name = "通知电话，必须是手机号码，座机不行")
    private String notifyPhone;

    @Schema(name = "是否支持非规则退款")
    private Boolean irregularRefund;

    @Schema(name = "最晚延迟时间点，2023-09-08 20:00:00")
    private String latestPeriodRulePoint;

//    @Schema(name = "手动接单开始时段，如：“01:00”")
//    private String acceptPeriodBegin;
//
//    @Schema(name = "手动接单结束时段，如：“23:00”")
//    private String acceptPeriodEnd;

}
