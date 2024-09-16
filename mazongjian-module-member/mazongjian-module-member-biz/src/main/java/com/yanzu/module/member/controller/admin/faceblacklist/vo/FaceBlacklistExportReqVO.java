package com.yanzu.module.member.controller.admin.faceblacklist.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 人脸黑名单 Excel 导出 Request VO，参数和 FaceBlacklistPageReqVO 是一致的")
@Data
public class FaceBlacklistExportReqVO {

    @Schema(description = "用户", example = "23317")
    private Long userId;

    @Schema(description = "门店", example = "10537")
    private Long storeId;

    @Schema(description = "照片", example = "https://www.iocoder.cn")
    private String photoUrl;
    @Schema(description = "照片数据", example = "https://www.iocoder.cn")
    private String photoData;

    @Schema(description = "人员guid", example = "23651")
    private String admitGuid;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
