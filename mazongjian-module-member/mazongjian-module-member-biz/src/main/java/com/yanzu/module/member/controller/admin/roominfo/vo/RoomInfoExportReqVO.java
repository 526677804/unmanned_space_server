package com.yanzu.module.member.controller.admin.roominfo.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 房间管理 Excel 导出 Request VO，参数和 RoomInfoPageReqVO 是一致的")
@Data
public class RoomInfoExportReqVO {

    @Schema(description = "房间名称", example = "王五")
    private String roomName;

    @Schema(description = "门店id", example = "14069")
    private Long storeId;

    @Schema(description = "房间类型", example = "2")
    private Integer type;

    @Schema(description = "单价", example = "20571")
    private BigDecimal price;

    @Schema(description = "排序位置", example = "5068")
    private Integer sortId;

    @Schema(description = "禁用开始时间")
    private LocalDateTime banTimeStart;

    @Schema(description = "禁用结束时间")
    private LocalDateTime banTimeEnd;

    @Schema(description = "总完成订单数")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] totalOrderNum;

    @Schema(description = "总收益")
    private BigDecimal totalMoney;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
