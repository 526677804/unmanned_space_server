package com.yanzu.module.member.controller.app.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "miniapp - 优惠券详情 Resp VO")
@Data
@ToString(callSuper = true)
public class AppCouponDetailRespVO {

    @Schema(description = "优惠券ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32081")
    private Long couponId;

    @Schema(description = "优惠券名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotNull(message = "优惠券名称不能为空")
    private String couponName;

    @Schema(description = "创建用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12270")
    private String createUserId;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY, timezone = DateUtils.TIME_ZONE_DEFAULT)
    private Date expriceTime;


    @Schema(description = "使用门槛", requiredMode = Schema.RequiredMode.REQUIRED, example = "24523")
    @NotNull(message = "使用门槛不能为空")
    private BigDecimal minUsePrice;

    @Schema(description = "优惠券面额", requiredMode = Schema.RequiredMode.REQUIRED, example = "14728")
    @NotNull(message = "优惠券面额不能为空")
    private BigDecimal price;

    @Schema(description = "适用门店id", example = "7851")
    private Long storeId;

    @Schema(description = "适用门店名称 逗号分隔", example = "7851")
    private String storeName;

    @Schema(description = "适用房间类型 值见字典 0表示不限制", example = "1")
    private Integer roomType;

    @Schema(description = "优惠券类型 值见字典", example = "1")
    private Integer type;

    @Schema(description = "状态 值见字典", example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;
}
