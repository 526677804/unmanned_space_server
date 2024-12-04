package com.yanzu.module.member.controller.app.meituanreserve.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DeskSoldTimePeriodsSub {

    @Schema(name = "业务类型，1-普通预订，2-包座，3-押金预订")
    private String bizType;

    @Schema(name = "占用时段信息 注：类型为对象类型，详见扩展参数time_period_items")
    private List<TimePeriodItemsSub> timePeriodItems;

}
