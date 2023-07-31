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

    @Schema(description = "可提现")
    private BigDecimal money;
    @Schema(description = "可提现")
    private BigDecimal totalMoney;
    @Schema(description = "已提现")
    private BigDecimal withdrawalMoney;


}
