package com.yanzu.module.member.controller.admin.faceblacklist.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Schema(description = "管理后台 - 人脸黑名单 Response VO")
@Data
@ToString(callSuper = true)
public class FaceBlacklistRespVO  {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9760")
    private Long blacklistId;

    @Schema(description = "门店名称", example = "10537")
    private String storeName;

    @Schema(description = "照片URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String photoUrl;

    @Schema(description = "照片数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String photoData;

    @Schema(description = "人员guid", requiredMode = Schema.RequiredMode.REQUIRED, example = "23651")
    private String admitGuid;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date createTime;

}
