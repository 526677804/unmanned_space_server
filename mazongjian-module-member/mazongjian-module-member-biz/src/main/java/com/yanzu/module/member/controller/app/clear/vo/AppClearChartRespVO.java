package com.yanzu.module.member.controller.app.clear.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Schema(description = "miniapp - 保洁统计图表  Response VO")
@Data
@ToString(callSuper = true)
public class AppClearChartRespVO {
    @Schema(description = "今日-已结单")
    private Integer todayJiedan;
    @Schema(description = "今日-进行中")
    private Integer todayJinxingzhong;
    @Schema(description = "今日-已完成")
    private Integer todayWancheng;

    @Schema(description = "本月-已结单")
    private Integer tomonthJiedan;
    @Schema(description = "本月-已完成")
    private Integer tomonthWancheng;
    @Schema(description = "本月-已驳回")
    private Integer tomonthBohui;

    @Schema(description = "总收入-已完成")
    private Integer totalWancheng;
    @Schema(description = "总收入-已结算")
    private Integer totalSettlementt;
    @Schema(description = "总收入-人民币")
    private Integer totalMoney;


}
