package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppStoreControlKTReqVO {

    @Schema(description = "房间id")
    @NotNull(message = "参数错误")
    private Long roomId;

    @Schema(description = "控制命令")
    @NotNull(message = "命令不能为空")
    private String cmd;

}
