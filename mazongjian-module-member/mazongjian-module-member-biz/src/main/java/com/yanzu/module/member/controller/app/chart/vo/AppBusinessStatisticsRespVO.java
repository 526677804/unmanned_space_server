package com.yanzu.module.member.controller.app.chart.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.chart.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/27 10:48
 */
@Schema(description = "miniapp - 获取经营统计数据 Response VO")
@Data
@ToString(callSuper = true)
public class AppBusinessStatisticsRespVO {

    @Schema(description = "总收入")
    private BigDecimal total;

    @Schema(description = "微信收入")
    private BigDecimal money;

    @Schema(description = "团购收入")
    private BigDecimal tgMoney;

    @Schema(description = "累积订单数")
    private Integer orderCount;

}
