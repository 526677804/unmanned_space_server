package com.yanzu.module.system.controller.admin.tenant.vo.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 租户更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TenantUpdateReqVO extends TenantBaseVO {

    @Schema(description = "租户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "租户编号不能为空")
    private Long id;

    @Schema(description = "新密码")
    @Length(min = 8, max = 16, message = "密码长度为 8-16 位")
    private String uppassword;

}
