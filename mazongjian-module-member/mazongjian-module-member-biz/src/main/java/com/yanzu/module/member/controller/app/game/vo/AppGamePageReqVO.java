package com.yanzu.module.member.controller.app.game.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.game.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 17:41
 */
@Schema(description = "miniapp - 获取线上组局列表 Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppGamePageReqVO extends PageParam {

    @Schema(description = "门店ID")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "状态 值见枚举")
    private Integer status;

    @Schema(hidden = true)
    private Long currentUserId;
}
