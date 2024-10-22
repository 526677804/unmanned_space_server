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
 * @DATE: 2023/7/26 15:25
 */
@Schema(description = "miniapp - 订单续费Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRenewalReqVO {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单不能为空")
    private Long orderId;

    @Schema(description = "订单结束时间 yyyy-MM-dd HH:mm", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-30 18:11:11")
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @NotNull(message = "订单结束时间不能为空")
    private Date endTime;

    @Schema(description = "支付方式 值见字典", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "支付方式不能为空")
    private Integer payType;

    @Schema(description = "团购券码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String groupPayNo;

    @Schema(description = "套餐id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pkgId;

    @Schema(description = "订单号 预下单接口返回的", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "下单的userId", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;


    @Schema(description = "优惠券ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long couponId;

}
