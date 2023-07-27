package com.yanzu.module.member.controller.admin.discountrules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 充值优惠规则管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DiscountRulesUpdateReqVO extends DiscountRulesBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25607")
    @NotNull(message = "ID不能为空")
    private Long discountId;

}
