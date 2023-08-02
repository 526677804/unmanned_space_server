package com.yanzu.module.member.controller.app.manager.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.manager.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/27 10:23
 */
@Schema(description = "miniapp - 管理员驳回保洁信息订单 Req VO")
@Data
@ToString(callSuper = true)
public class AppComplaintClearInfoReqVO {

    @Schema(description = "clearId")
    @NotNull(message = "数据id不能为空")
    private Long clearId;

    @Schema(description = "驳回的原因")
    private String complaintDesc;

    @Schema(description = "是否驳回")
    @NotNull(message = "必须传是否驳回")
    private Boolean complaint;

}
