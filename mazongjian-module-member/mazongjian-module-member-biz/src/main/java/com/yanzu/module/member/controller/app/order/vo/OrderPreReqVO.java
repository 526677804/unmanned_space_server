package com.yanzu.module.member.controller.app.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 18:25
 */
@Schema(description = "miniapp - 预下单订单Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPreReqVO implements Serializable {

    @Schema(description = "不需要传", example = "1", hidden = true)
    private Long userId;

    @Schema(description = "支付类型")
    private Integer payType;

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "房间id不能为空")
    private Long roomId;

    @Schema(description = "优惠券Id", example = "1")
    private Long couponId;

    @Schema(description = "续费传", example = "1")
    private Long orderId;

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


    @Schema(description = "套餐id")
    private Long pkgId;


    @Schema(description = "是否通宵局", example = "false")
    private boolean nightLong;

}
