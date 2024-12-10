package com.yanzu.module.member.controller.app.manager.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppManagerUserCouponListReqVO {

    @Schema(description = "用户id")
    @NotNull(message = "用户不能为空")
    private Long userId;


    @Schema(hidden = true)
    private String storeIds;
}
