package com.yanzu.module.member.controller.app.chart.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.chart.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2024/4/18 18:05
 */
@Data
public class AppIncomeStatisticsRespVO {

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "金额")
    private BigDecimal price;

    @Schema(description = "支付类型")
    private String payType;

    @Schema(description = "时间")
    @JsonFormat(pattern = DateUtils.FORMAT_MONTH_DAY_HOUR_MINUTE, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_MONTH_DAY_HOUR_MINUTE)
    private Date createTime;


}
