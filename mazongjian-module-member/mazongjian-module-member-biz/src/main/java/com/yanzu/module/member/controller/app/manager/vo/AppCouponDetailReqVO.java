package com.yanzu.module.member.controller.app.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.checkerframework.common.value.qual.MinLen;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Schema(description = "miniapp - 优惠券详情保存 Req VO")
@Data
@ToString(callSuper = true)
public class AppCouponDetailReqVO {

    @Schema(description = "优惠券ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32081")
    private Long couponId;

    @Schema(description = "优惠券名称 大于5个字", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotNull(message = "优惠券名称不能为空")
    @MinLen(value = 5)
    private String couponName;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY, timezone = TIME_ZONE_DEFAULT)
    @NotNull(message = "过期时间不能为空")
    private Date expriceTime;


    @Schema(description = "使用门槛", requiredMode = Schema.RequiredMode.REQUIRED, example = "24523")
    @NotNull(message = "使用门槛不能为空")
    private BigDecimal minUsePrice;

    @Schema(description = "优惠券面额", requiredMode = Schema.RequiredMode.REQUIRED, example = "14728")
    @NotNull(message = "优惠券面额不能为空")
    private BigDecimal price;

    @Schema(description = "适用门店id", example = "1")
    @NotNull(message = "适用门店不能为空")
    private Long storeId;


    @Schema(description = "适用房间类型 值见字典 空表示不限制", example = "1")
    private Integer roomType;

    @Schema(description = "优惠券类型 值见字典", example = "1")
    @NotNull(message = "优惠券类型不能为空")
    private Integer type;


}
