package com.yanzu.module.member.controller.app.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.manager.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2024/3/12 10:00
 */
@Data
public class AppChangeOrderTimeReqVO {

    @Schema(description = "")
    @NotNull(message = "订单编号不能为空")
    private Long orderId;

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "房间不能为空")
    private Long roomId;


    @Schema(description = "订单开始时间 yyyy-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-30 11:11:11")
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @NotNull(message = "订单开始时间不能为空")
    private Date startTime;

    @Schema(description = "订单结束时间 yyyy-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-30 18:11:11")
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @NotNull(message = "订单结束时间不能为空")
    private Date endTime;

}
