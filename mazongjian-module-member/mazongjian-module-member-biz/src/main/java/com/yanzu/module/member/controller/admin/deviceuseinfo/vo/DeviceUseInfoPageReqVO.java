package com.yanzu.module.member.controller.admin.deviceuseinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备使用记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceUseInfoPageReqVO extends PageParam {

    @Schema(description = "用户id", example = "3090")
    private Long userId;

    @Schema(description = "设备id", example = "27533")
    private Long deviceId;

    @Schema(description = "设备sn")
    private String deviceNo;

    @Schema(description = "命令")
    private String cmd;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
