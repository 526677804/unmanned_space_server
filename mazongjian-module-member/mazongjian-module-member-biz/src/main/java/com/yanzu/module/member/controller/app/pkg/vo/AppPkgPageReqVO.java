package com.yanzu.module.member.controller.app.pkg.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.pojo.PageParam;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AppPkgPageReqVO extends PageParam {


    @Schema(description = "门店id")
    @NotNull(message = "门店不能为空")
    private Long storeId;


    @Schema(description = "房间id")
    private Long roomId;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = DateUtils.TIME_ZONE_DEFAULT)
    private Date startTime;


    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = DateUtils.TIME_ZONE_DEFAULT)
    private Date endTime;

    @Schema(description = "小时（时长）")
    private Integer hours;
}
