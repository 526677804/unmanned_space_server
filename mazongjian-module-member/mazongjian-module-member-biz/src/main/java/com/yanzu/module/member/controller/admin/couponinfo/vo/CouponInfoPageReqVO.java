package com.yanzu.module.member.controller.admin.couponinfo.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 优惠券管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CouponInfoPageReqVO extends PageParam {

    @Schema(description = "持有用户", example = "13545")
    private String userId;

    @Schema(description = "创建用户", example = "12270")
    private String createUserId;

    @Schema(description = "过期时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] expriceTime;

    @Schema(description = "优惠券名称", example = "赵六")
    private String couponName;

    @Schema(description = "使用门槛", example = "24523")
    private BigDecimal minUsePrice;

    @Schema(description = "优惠券面额", example = "14728")
    private BigDecimal price;

    @Schema(description = "使用门店", example = "7851")
    private Long storeId;

    @Schema(description = "优惠券类型", example = "1")
    private Integer type;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
