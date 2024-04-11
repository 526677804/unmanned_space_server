package com.yanzu.module.member.controller.app.chart.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.chart.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/27 10:40
 */
@Schema(description = "miniapp - 获取营业额数据chart Response VO")
@Data
@ToString(callSuper = true)
public class AppRevenueChartRespVO {

//    @Schema(description = "收入")
//    private BigDecimal money;

    @Schema(description = "微信总收入")
    private BigDecimal wxTotalMoney;

    @Schema(description = "团购总收入")
    private BigDecimal groupTotalMoney;

    @Schema(description = "美团总收入")
    private BigDecimal mtTotalMoney;

    @Schema(description = "抖音总收入")
    private BigDecimal dyTotalMoney;

    @Schema(description = "总收入")
    private BigDecimal totalMoney;

    @Schema(description = "总订单数")
    private Integer totalOrder;


//    @Schema(description = "已提现")
//    private BigDecimal withdrawalMoney;


}
