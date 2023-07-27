package com.yanzu.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.user.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 16:45
 */
@Schema(description = "miniapp - 用户提交加盟信息 Request VO")
@Data
@ToString(callSuper = true)
public class AppSaveFranchiseInfoReqVO {

    private String city;


}
