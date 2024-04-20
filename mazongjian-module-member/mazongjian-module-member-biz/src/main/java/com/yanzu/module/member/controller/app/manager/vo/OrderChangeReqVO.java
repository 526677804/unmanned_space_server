package com.yanzu.module.member.controller.app.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
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
 * @DATE: 2023/7/26 15:25
 */
@Schema(description = "miniapp - 订单续费Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderChangeReqVO {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单不能为空")
    private Long orderId;


//    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED)
//    @NotNull(message = "房间不能为空")
//    private Long roomId;

    @Schema(description = "订单新开锁时间 yyyy-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-30 18:11:11")
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @NotNull(message = "订单新开始时间不能为空")
    private Date startTime;

}
