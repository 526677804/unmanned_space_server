package com.yanzu.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.user.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 16:58
 */
@Schema(description = "miniapp - 用户加盟信息提交 Request VO")
@Data
@ToString(callSuper = true)
public class AppFranchiseInfoReqVO {


    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Schema(description = "联系人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String contactName;

    @Schema(description = "联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactPhone;

    @Schema(description = "留言", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

}
