package com.yanzu.module.member.controller.app.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.pojo.PageParam;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.manager.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/31 16:02
 */
@Schema(description = "miniapp - 获取提现记录分页 Req VO")
@Data
@ToString(callSuper = true)
public class AppWithdrawalPageReqVO extends PageParam {


    @Schema(hidden = true)
    private Long userId;

    @Schema(description = "开始时间 yyyy-MM-dd")
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    private Date startTime;

    @Schema(description = "截止时间  yyyy-MM-dd")
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    private Date endTime;

}
