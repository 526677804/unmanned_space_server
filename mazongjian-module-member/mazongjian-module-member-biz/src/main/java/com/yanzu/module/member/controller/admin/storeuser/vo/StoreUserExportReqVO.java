package com.yanzu.module.member.controller.admin.storeuser.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 门店用户管理 Excel 导出 Request VO，参数和 StoreUserPageReqVO 是一致的")
@Data
public class StoreUserExportReqVO {

    @Schema(description = "收入")
    private BigDecimal money;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "门店id", example = "14718")
    private Long storeId;

    @Schema(description = "用户id", example = "13230")
    private Long userId;

    @Schema(description = "类型", example = "1")
    private Integer type;

    @Schema(description = "状态", example = "2")
    private Integer status;

}
