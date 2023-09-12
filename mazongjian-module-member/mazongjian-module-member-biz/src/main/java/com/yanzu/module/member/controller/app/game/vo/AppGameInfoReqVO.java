package com.yanzu.module.member.controller.app.game.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Schema(description = "miniapp - 保存组局信息 Response VO")
@Data
@ToString(callSuper = true)
public class AppGameInfoReqVO {


    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32343")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "房间ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14759")
    @NotNull(message = "房间不能为空")
    private Long roomId;

    @Schema(description = "规则描述", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "规则描述不能为空")
    private String ruleDesc;

    @Schema(description = "玩家数量")
    @NotNull(message = "玩家数量不能为空")
    @Min(value = 2, message = "玩家数量不能小于2")
    @Max(value = 4, message = "玩家数量不能大于4")
    private Integer userNum;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE)
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE)
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

}
