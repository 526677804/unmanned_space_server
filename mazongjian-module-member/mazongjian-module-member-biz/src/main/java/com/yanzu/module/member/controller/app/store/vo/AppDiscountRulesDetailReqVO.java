package com.yanzu.module.member.controller.app.store.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "miniapp - 门店充值优惠规则保存 Req VO")
@Data
@ToString(callSuper = true)
public class AppDiscountRulesDetailReqVO {

    @Schema(description = "Id")
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
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private Date expriceTime;


}
