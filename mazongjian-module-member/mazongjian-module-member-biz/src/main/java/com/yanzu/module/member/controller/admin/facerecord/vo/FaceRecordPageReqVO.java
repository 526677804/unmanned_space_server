package com.yanzu.module.member.controller.admin.facerecord.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 人脸识别记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FaceRecordPageReqVO extends PageParam {

    @Schema(description = "门店Id")
    private Long storeId;

    @Schema(description = "设备编号")
    private String deviceSn;

    @Schema(description = "人员guid", example = "3140")
    private String admitGuid;

    @Schema(description = "识别时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] showTime;

    @Schema(description = "比对结果", example = "2")
    private Integer type;


}
