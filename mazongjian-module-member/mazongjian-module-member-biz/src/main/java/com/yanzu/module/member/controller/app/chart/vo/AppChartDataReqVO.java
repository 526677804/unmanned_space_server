package com.yanzu.module.member.controller.app.chart.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.annotations.Param;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.chart.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/27 10:46
 */

@Schema(description = "miniapp - 获取统计图表数据 Req VO")
@Data
@ToString(callSuper = true)
public class AppChartDataReqVO {

    @Schema(hidden = true)
    private Long userId;

    @Schema(hidden = true)
    private Integer groupPayType;

    @Schema(description = "门店id")
    private Long storeId;

    @Schema(description = "开始时间", example = "2023-07-25 11:11:11")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY, timezone = TIME_ZONE_DEFAULT)
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @Schema(description = "截止时间", example = "2023-07-31 11:11:11")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY, timezone = TIME_ZONE_DEFAULT)
    @NotNull(message = "截止时间")
    private Date endTime;


}
