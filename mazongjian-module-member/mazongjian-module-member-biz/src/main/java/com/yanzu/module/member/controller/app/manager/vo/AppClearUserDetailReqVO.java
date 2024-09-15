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
 * @DATE: 2023/7/27 10:01
 */
@Schema(description = "miniapp - 管理员保存保洁员用户 Req VO")
@Data
@ToString(callSuper = true)
public class AppClearUserDetailReqVO {

//    @Schema(description = "数据的id,不是userId,编辑的时候传", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
//    private Long id;

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotNull(message = "真实姓名不能为空")
    private String name;

    @Schema(description = "用户手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotNull(message = "手机号不能为空")
    @Mobile
    private String mobile;

    @Schema(description = "是否超管")
    private Boolean isAdmin;




}
