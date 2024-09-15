package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceInfoPageReqVO extends PageParam {

    @Schema(description = "设备sn")
    private String deviceSn;

    @Schema(description = "设备类型", example = "1")
    private Integer type;

    @Schema(description = "房间id", example = "2367")
    private Long roomId;

    @Schema(description = "门店id", example = "8462")
    private Long storeId;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "是否多房间共享设备")
    private Boolean share;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
