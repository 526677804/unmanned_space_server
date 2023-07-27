package com.yanzu.module.member.controller.app.game.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.game.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 17:44
 */
@Schema(description = "miniapp - 组局用户信息 Response VO")
@Data
@ToString(callSuper = true)
public class AppGameUserListRespVO {

    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "头像url")
    private String avatar;
    @Schema(description = "昵称")
    private String nickname;


}
