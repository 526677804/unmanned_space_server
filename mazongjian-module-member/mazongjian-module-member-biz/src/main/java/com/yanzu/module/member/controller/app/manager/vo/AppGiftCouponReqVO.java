package com.yanzu.module.member.controller.app.manager.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.manager.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/8/28 21:50
 */
@Schema(description = "miniapp - 管理员赠送优惠券 Req VO")
@Data
@ToString(callSuper = true)
public class AppGiftCouponReqVO {

    @Schema(description = "优惠券ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32081")
    @NotNull(message = "优惠券id不能为空")
    private Long couponId;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32081")
    @NotNull(message = "用户id不能为空")
    private Long userId;
}
