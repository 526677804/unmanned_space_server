package com.yanzu.module.member.controller.admin.orderinfo.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 订单管理 Excel 导出 Request VO，参数和 OrderInfoPageReqVO 是一致的")
@Data
public class OrderInfoExportReqVO {

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "房间id", example = "2319")
    private Long roomId;

    @Schema(description = "用户id", example = "4765")
    private Long userId;

    @Schema(description = "订单开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "订单结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "订单价格", example = "1698")
    private BigDecimal price;

    @Schema(description = "支付方式", example = "1")
    private Integer payType;

    @Schema(description = "团购券码")
    private String groupPayNo;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
