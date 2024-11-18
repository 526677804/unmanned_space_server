package com.yanzu.module.member.controller.app.manager.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.manager.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/27 9:50
 */
@Schema(description = "miniapp - 会员黑名单列表 Resp VO")
@Data
@ToString(callSuper = true)
public class AppVipBlacklistRespVO {

    @Schema(description = "id 数据的id,不是userId")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "门店Id")
    private Long storeId;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    private String nickname;

    @Schema(description = "用户头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "/infra/file/get/35a12e57-4297-4faa-bf7d-7ed2f211c952")
    private String avatar;

    @Schema(description = "用户手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    private String mobile;

    @Schema(description = "添加时间 时间戳", requiredMode = Schema.RequiredMode.REQUIRED, example = "1731918674000")
    private Date addTime;



}
