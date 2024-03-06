package com.yanzu.module.member.controller.admin.franchiseinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 加盟信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FranchiseInfoUpdateReqVO extends FranchiseInfoBaseVO {

    @NotNull(message = "ID不能为空")
    private Long id;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "城市名称不能为空")
    private String city;

    @Schema(description = "联系人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotNull(message = "联系人姓名不能为空")
    private String contactName;

    @Schema(description = "联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "联系人电话不能为空")
    private String contactPhone;

    @Schema(description = "留言", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "留言不能为空")
    private String message;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
