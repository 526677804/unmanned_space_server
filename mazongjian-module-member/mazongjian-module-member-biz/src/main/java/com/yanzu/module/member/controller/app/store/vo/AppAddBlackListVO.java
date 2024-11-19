package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AppAddBlackListVO {

    @Schema(description = "用户手机号")
    @NotNull(message = "用户手机号不能为空")
    @Size(message = "手机号要求为11位",min = 11,max = 11)
    private String phone;

    @Schema(description = "门店id")
    @NotNull(message = "门店id不能为空")
    private Long storeId;

}
