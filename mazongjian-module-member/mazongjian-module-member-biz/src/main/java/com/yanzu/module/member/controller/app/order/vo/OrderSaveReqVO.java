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
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 18:25
 */
@Schema(description = "miniapp - 提交订单Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSaveReqVO {

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

    @Schema(description = "支付方式 1微信 2余额 3团购 4套餐", example = "1")
    @NotNull(message = "支付方式不能为空")
    private Integer payType;

    @Schema(description = "订单号 预下单返回的  必填")
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "团购券码 填了团购券时，其他支付方式均不生效")
    private String groupPayNo;

    @Schema(description = "优惠券Id", example = "31071")
    private Long couponId;

    @Schema(description = "套餐id", example = "1")
    private Long pkgId;

    @Schema(description = "下单的userId", example = "266")
    private Long userId;

    @Schema(description = "是否通宵局", example = "false")
    private Boolean nightLong;
}
