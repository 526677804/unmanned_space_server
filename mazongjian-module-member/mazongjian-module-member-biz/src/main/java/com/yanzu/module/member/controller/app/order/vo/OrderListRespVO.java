package com.yanzu.module.member.controller.app.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.pojo.PageParam;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 14:30
 */
@Schema(description = "miniapp - 订单分页列表VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListRespVO {
    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2248")
    private Long orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "第一总店")
    private Long storeId;
    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "第一总店")
    private String storeName;

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "第一总店")
    private Long roomId;


    @Schema(description = "房间类型  值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer roomType;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "VIP包间")
    private String roomName;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4765")
    private Long userId;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "4765")
    private String nickname;

    @Schema(description = "订单开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date startTime;

    @Schema(description = "订单结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date endTime;

    @Schema(description = "实际支付价格", example = "6888")
    private BigDecimal payPrice;

    @Schema(description = "退款价格", example = "8062")
    private BigDecimal refundPrice;

    @Schema(description = "状态 值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date createTime;

}
