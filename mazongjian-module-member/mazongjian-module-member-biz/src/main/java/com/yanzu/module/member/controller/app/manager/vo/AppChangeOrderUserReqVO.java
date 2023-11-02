package com.yanzu.module.member.controller.app.manager.vo;

import com.yanzu.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.manager.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/25 18:41
 */
@Schema(description = "miniapp - 管理员修改订单用户 Req VO")
@Data
@ToString(callSuper = true)
public class AppChangeOrderUserReqVO {

    @Schema(description = "用户手机号")
    @Mobile
    @NotNull(message = "用户手机号不能为空")
    private String mobile;

    @Schema(description = "订单id")
    @NotNull(message = "订单id不能为空")
    private Long orderId;


}
