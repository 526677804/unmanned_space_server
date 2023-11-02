package com.yanzu.module.member.controller.admin.groupPay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 团购支付信息 Excel 导出 Request VO，参数和 GroupPayInfoPageReqVO 是一致的")
@Data
public class GroupPayInfoExportReqVO {

    @Schema(description = "团购券名称", example = "李四")
    private String groupName;

    @Schema(description = "团购券编码")
    private String groupNo;

    @Schema(description = "价格", example = "11090")
    private BigDecimal groupPayPrice;

    @Schema(description = "团购券类型", example = "2")
    private Integer groupPayType;

    @Schema(description = "门店", example = "15964")
    private Long storeId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
