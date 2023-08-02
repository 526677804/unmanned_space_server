package com.yanzu.module.member.controller.admin.deviceuseinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备使用记录 Excel 导出 Request VO，参数和 DeviceUseInfoPageReqVO 是一致的")
@Data
public class DeviceUseInfoExportReqVO {

    @Schema(description = "用户id", example = "10776")
    private Long userId;

    @Schema(description = "门店id", example = "30308")
    private Long storeId;

    @Schema(description = "房间id", example = "14482")
    private Long roomId;

    @Schema(description = "命令")
    private String cmd;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
