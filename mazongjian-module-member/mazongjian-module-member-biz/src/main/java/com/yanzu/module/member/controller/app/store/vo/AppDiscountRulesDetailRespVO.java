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

@Schema(description = "miniapp - 门店充值优惠规则详情 Response VO")
@Data
@ToString(callSuper = true)
public class AppDiscountRulesDetailRespVO {

    @Schema(description = "discountId")
    private Long discountId;

    @Schema(description = "门店Id")
    @NotNull(message = "门店不能为空")
    private Long storeId;

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

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;
}
