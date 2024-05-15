package com.yanzu.module.member.controller.app.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import com.yanzu.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 18:25
 */
@Schema(description = "miniapp - 管理员提交订单Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSubmitReqVO {

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2319")
    @NotNull(message = "房间id不能为空")
    private Long roomId;

    @Schema(description = "订单开始时间 yyyy-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @NotNull(message = "订单开始时间不能为空")
    private Date startTime;

    @Schema(description = "订单结束时间 yyyy-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @NotNull(message = "订单结束时间不能为空")
    private Date endTime;

    @Schema(description = "下单的手机号", example = "266")
    @Mobile
    private String mobile;

}