package com.yanzu.module.member.controller.admin.franchiseinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 加盟信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FranchiseInfoRespVO extends FranchiseInfoBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12936")
    private Long id;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "联系人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String contactName;

    @Schema(description = "联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactPhone;

    @Schema(description = "留言", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
