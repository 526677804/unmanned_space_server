package com.yanzu.module.member.controller.admin.usermoneybill.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户账单明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserMoneyBillPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "10013")
    private Long userId;

    @Schema(description = "类型", example = "2")
    private Integer type;

    @Schema(description = "金额")
    private BigDecimal money;

    @Schema(description = "金额类型", example = "1")
    private Integer moneyType;

    @Schema(description = "当时总账户余额")
    private BigDecimal totalMoney;

    @Schema(description = "当时总赠送余额")
    private BigDecimal totalGiftMoney;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
