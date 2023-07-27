package com.yanzu.module.member.controller.admin.couponinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 优惠券管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CouponInfoRespVO extends CouponInfoBaseVO {

    @Schema(description = "优惠券ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32081")
    private Long couponId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
