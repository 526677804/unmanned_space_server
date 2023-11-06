package com.yanzu.module.member.controller.app.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.manager.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/27 10:20
 */
@Schema(description = "miniapp - 管理员结算保洁员费用 Req VO")
@Data
@ToString(callSuper = true)
public class AppSettlementClearUserReqVO {

    @Schema(description = "保洁员用户id")
    @NotNull(message = "保洁员id不能为空")
    private Long userId;

    @Schema(description = "门店id")
    @NotNull(message = "门店id不能为空")
    private Long storeId;

    @Schema(description = "结算金额")
    @NotNull(message = "结算金额不能为空")
    private BigDecimal money;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private Date startTime;

    @Schema(description = "截止时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private Date endTime;

}
