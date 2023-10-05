package com.yanzu.module.member.controller.app.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Schema(description = "miniapp - 用户账单明细 Response VO")
@Data
@ToString(callSuper = true)
public class AppUserMoneyBillRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25644")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10013")
    private Long userId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "测试店铺")
    private String storeName;

    @Schema(description = "类型 值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal money;

    @Schema(description = "金额类型 值见字典", example = "1")
    private Integer moneyType;

    @Schema(description = "当时总账户余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalMoney;

    @Schema(description = "当时总赠送余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalGiftMoney;

    @Schema(description = "备注", example = "这是备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date createTime;

}
