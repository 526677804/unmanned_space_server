package com.yanzu.module.member.controller.admin.faceblacklist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 人脸黑名单 Response VO")
@Data
@ToString(callSuper = true)
public class FaceBlacklistRespVO  {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9760")
    private Long blacklistId;

    @Schema(description = "门店名称", example = "10537")
    private String storeName;

    @Schema(description = "照片", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String photoUrl;

    @Schema(description = "人员guid", requiredMode = Schema.RequiredMode.REQUIRED, example = "23651")
    private String admitGuid;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
