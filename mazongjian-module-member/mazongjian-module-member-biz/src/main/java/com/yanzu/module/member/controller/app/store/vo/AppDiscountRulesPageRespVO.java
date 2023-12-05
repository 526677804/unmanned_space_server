package com.yanzu.module.member.controller.app.store.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "miniapp - 门店充值优惠规则 Response VO")
@Data
@ToString(callSuper = true)
public class AppDiscountRulesPageRespVO {

    @Schema(description = "Id")
    private Long discountId;

    @Schema(description = "门店Id")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "支付金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "支付金额不能为空")
    private BigDecimal payMoney;

    @Schema(description = "赠送金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "赠送金额不能为空")
    private BigDecimal giftMoney;

    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "过期时间不能为空")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    private Date expriceTime;

    @Schema(description = "状态 值见字典", example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date createTime;
}
