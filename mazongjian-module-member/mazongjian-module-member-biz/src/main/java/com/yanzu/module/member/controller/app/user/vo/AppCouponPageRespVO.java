package com.yanzu.module.member.controller.app.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.user.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 18:10
 */
@Schema(description = "miniapp - 用户优惠券列表 Resp VO")
@Data
@ToString(callSuper = true)
public class AppCouponPageRespVO {

    @Schema(description = "优惠券ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32081")
    private Long couponId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = DateUtils.TIME_ZONE_DEFAULT)
    private Date createTime;

    @Schema(description = "持有用户", example = "13545")
    private String userId;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "过期时间不能为空")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY, timezone = DateUtils.TIME_ZONE_DEFAULT)
    private Date expriceTime;

    @Schema(description = "优惠券名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "新人福利满50-10")
    @NotNull(message = "优惠券名称不能为空")
    private String couponName;

    @Schema(description = "适用门槛", requiredMode = Schema.RequiredMode.REQUIRED, example = "24523")
    @NotNull(message = "适用门槛不能为空")
    private BigDecimal minUsePrice;

    @Schema(description = "优惠券面额", requiredMode = Schema.RequiredMode.REQUIRED, example = "14728")
    @NotNull(message = "优惠券面额不能为空")
    private BigDecimal price;

    @Schema(description = "适用门店id", example = "7851")
    private Long storeId;

    @Schema(description = "适用门店名称 逗号分隔", example = "7851")
    private String storeName;

    @Schema(description = "适用房间类型 值见字典 ", example = "1")
    private Integer roomType;

    @Schema(description = "优惠券类型 值见字典", example = "1")
    private Integer type;

    @Schema(description = "状态 值见字典", example = "1")
    private Integer status;

    @Schema(description = "被使用的订单id", example = "1")
    private Long orderId;

    @Schema(description = "是否使用（下单的时候判断）", example = "1")
    private boolean enable;

}