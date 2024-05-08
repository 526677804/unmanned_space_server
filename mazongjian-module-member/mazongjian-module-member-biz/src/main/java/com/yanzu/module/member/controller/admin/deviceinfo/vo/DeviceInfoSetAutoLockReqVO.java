package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class DeviceInfoSetAutoLockReqVO {


    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "设备id不能为空")
    private Long deviceId;


    @Schema(description = "自动关锁时间")
    @NotNull(message = "自动关锁时间不能为空")
    private Integer secend;




}
