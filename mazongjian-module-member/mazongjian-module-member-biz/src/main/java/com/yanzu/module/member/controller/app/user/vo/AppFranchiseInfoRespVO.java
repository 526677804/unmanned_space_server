package com.yanzu.module.member.controller.app.user.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.user.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 16:42
 */
@Schema(description = "miniapp - 用户加盟信息 Resp VO")
@Data
@ToString(callSuper = true)
public class AppFranchiseInfoRespVO {

    @Schema(description = "加盟电话")
    private String franchise;


    @Schema(description = "是否已经填写")
    private Boolean isCommit;

}
