package com.yanzu.module.member.controller.admin.facerecord.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Schema(description = "管理后台 - 人脸识别记录 Response VO")
@Data
@ToString(callSuper = true)
public class FaceRecordRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31775")
    private Long id;

    @Schema(description = "门店")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "设备编号")
    private String deviceSn;

    @Schema(description = "识别记录ID", example = "477")
    private Long faceId;

    @Schema(description = "人员guid", example = "3140")
    private String admitGuid;

    @Schema(description = "照片", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String photoUrl;

    @Schema(description = "照片数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String photoData;

    @Schema(description = "识别时间")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date showTime;

    @Schema(description = "比对结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
