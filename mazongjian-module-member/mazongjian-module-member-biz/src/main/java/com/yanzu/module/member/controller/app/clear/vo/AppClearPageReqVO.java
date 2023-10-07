package com.yanzu.module.member.controller.app.clear.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "miniapp - 保洁任务信息 Req VO")
@Data
@ToString(callSuper = true)
public class AppClearPageReqVO extends PageParam {

    @Schema(description = "用户id",hidden = true)
    private Long userId;

    @Schema(description = "门店Id")
    private Long storeId;

    @Schema(description = "门店Ids", hidden = true)
    private String storeIds;

    @Schema(description = "状态 值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private Date startTime;

    @Schema(description = "截止时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private Date endTime;
}
