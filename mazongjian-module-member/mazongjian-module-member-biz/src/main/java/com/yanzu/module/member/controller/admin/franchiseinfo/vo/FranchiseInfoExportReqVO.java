package com.yanzu.module.member.controller.admin.franchiseinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 加盟信息 Excel 导出 Request VO，参数和 FranchiseInfoPageReqVO 是一致的")
@Data
public class FranchiseInfoExportReqVO {

    @Schema(description = "城市名称")
    private String city;

    @Schema(description = "联系人姓名", example = "王五")
    private String contactName;

    @Schema(description = "留言")
    private String message;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
