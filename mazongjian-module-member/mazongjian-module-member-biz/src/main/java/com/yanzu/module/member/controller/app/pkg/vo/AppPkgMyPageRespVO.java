package com.yanzu.module.member.controller.app.pkg.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Data
public class AppPkgMyPageRespVO {

    @Schema(description = "套餐id")
    private Long pkgId;

    @Schema(description = "套餐名称")
    private String pkgName;

    @Schema(description = "抵扣时长")
    private Integer hours;

    @Schema(description = "门店id")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "房间类型 空表示不限")
    private Integer roomType;

    @Schema(description = "可用时间 0-23数字，空表示不限")
    private List<Integer> enableTime;

    @Schema(description = "可用星期 1-7 数字，空表示不限")
    private List<Integer> enableWeek;

    @Schema(description = "节假日可用")
    private Boolean enableHoliday;

    @Schema(description = "状态 0待使用 1已使用 2过期")
    private Integer status;

    @Schema(description = "过期时间")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY, timezone = TIME_ZONE_DEFAULT)
    private Date expireDate;

    @Schema(description = "销售价格")
    private BigDecimal price;


    @Schema(description = "购买时间")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date createTime;

    @Schema(description = "支持余额下单")
    private Boolean balanceBuy;

}
