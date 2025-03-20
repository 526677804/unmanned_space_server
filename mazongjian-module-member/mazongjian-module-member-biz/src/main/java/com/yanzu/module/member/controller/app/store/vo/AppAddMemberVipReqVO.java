package com.yanzu.module.member.controller.app.store.vo;

import com.yanzu.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppAddMemberVipReqVO {

    @Schema(description = "门店ID")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "手机号")
    @NotNull(message = "用户不能为空")
    @Mobile
    private String mobile;

    @Schema(description = "等级ID")
    @NotNull(message = "等级不能为空")
    private Byte vipLevel;

}
