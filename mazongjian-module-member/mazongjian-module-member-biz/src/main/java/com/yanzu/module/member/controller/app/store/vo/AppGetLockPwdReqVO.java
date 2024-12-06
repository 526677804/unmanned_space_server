package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppGetLockPwdReqVO {

    @Schema(description = "房间ID")
    private Long roomId;


    @Schema(description = "门店ID")
    @NotNull(message = "门店不能为空")
    private Long storeId;

}
