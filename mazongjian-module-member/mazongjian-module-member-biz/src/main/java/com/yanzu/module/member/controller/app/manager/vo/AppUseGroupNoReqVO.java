package com.yanzu.module.member.controller.app.manager.vo;

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
@Schema(description = "miniapp - 管理员验券 Req VO")
@Data
@ToString(callSuper = true)
public class AppUseGroupNoReqVO {
    @Schema(description = "门店id")
    @NotNull(message = "门店id不能为空")
    private Long storeId;

    @Schema(description = "团购券编码")
    @NotNull(message = "团购券编码不能为空")
    private String groupPayNo;


}
