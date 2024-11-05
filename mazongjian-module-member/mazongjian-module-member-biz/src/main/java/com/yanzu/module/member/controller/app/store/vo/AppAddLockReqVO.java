package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppAddLockReqVO {

    @Schema(description = "设备编号")
    @NotNull(message = "设备编号不能为空")
    private String deviceSn;

    @Schema(description = "门锁数据")
    @NotNull(message = "门锁数据不能为空")
    private String lockData;

    private String upData;
}
