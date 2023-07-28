package com.yanzu.module.member.controller.admin.bannerinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 广告管理 Excel 导出 Request VO，参数和 BannerInfoPageReqVO 是一致的")
@Data
public class BannerInfoExportReqVO {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "广告类型", example = "2")
    private Byte type;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
