package com.yanzu.module.member.controller.admin.couponinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 优惠券管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CouponInfoUpdateReqVO extends CouponInfoBaseVO {

    @Schema(description = "优惠券ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32081")
    @NotNull(message = "优惠券ID不能为空")
    private Long couponId;

}
