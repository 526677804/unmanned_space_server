package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备管理 Excel 导出 Request VO，参数和 DeviceInfoPageReqVO 是一致的")
@Data
public class DeviceInfoExportReqVO {

    @Schema(description = "设备sn")
    private String deviceSn;

    @Schema(description = "设备类型", example = "1")
    private Boolean type;

    @Schema(description = "房间id", example = "2367")
    private Long roomId;

    @Schema(description = "门店id", example = "8462")
    private Long storeId;

    @Schema(description = "状态", example = "2")
    private Boolean status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
