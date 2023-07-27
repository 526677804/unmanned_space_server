package com.yanzu.module.member.controller.app.index.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;
import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.index.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 17:14
 */
@Schema(description = "miniapp - 获取房间信息列表 Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppRoomInfoListReqVO {

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "日期 yyyy-MM-dd")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private Data day;

}
