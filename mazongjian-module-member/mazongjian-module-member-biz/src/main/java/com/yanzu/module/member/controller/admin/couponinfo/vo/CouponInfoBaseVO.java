package com.yanzu.module.member.controller.admin.couponinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 优惠券管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class CouponInfoBaseVO {

    @Schema(description = "持有用户", example = "13545")
    private String userId;

    @Schema(description = "创建用户", requiredMode = Schema.RequiredMode.REQUIRED, example = "12270")
    @NotNull(message = "创建用户不能为空")
    private String createUserId;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "过期时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime expriceTime;

    @Schema(description = "优惠券名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotNull(message = "优惠券名称不能为空")
    private String couponName;

    @Schema(description = "使用门槛", requiredMode = Schema.RequiredMode.REQUIRED, example = "24523")
    @NotNull(message = "使用门槛不能为空")
    private BigDecimal minUsePrice;

    @Schema(description = "优惠券面额", requiredMode = Schema.RequiredMode.REQUIRED, example = "14728")
    @NotNull(message = "优惠券面额不能为空")
    private BigDecimal price;

    @Schema(description = "使用门店", example = "7851")
    private Long storeId;

    @Schema(description = "使用房间", example = "14148")
    private Long roomId;

    @Schema(description = "优惠券类型", example = "1")
    private Integer type;

    @Schema(description = "状态", example = "1")
    private Integer status;

}
