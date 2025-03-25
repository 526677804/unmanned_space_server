package com.yanzu.module.member.controller.app.reserve.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TimePeriodItemsSub {

     @Schema(description = "开始时间（从0:00开始至开始时间的分钟数，如8:00开始即为 480 【8*60】）")
    private Integer beginMinutes;

     @Schema(description = "结束时间（从0:00开始至结束的分钟数）")
    private Integer endMinutes;

     @Schema(description = "开始日期（时间戳毫秒数）")
    private Long beginTime;

     @Schema(description = "结束日期（时间戳毫秒数）")
    private Long endTime;
}
