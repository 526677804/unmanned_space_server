package com.yanzu.module.member.service.iot.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IotDeviceAddBlacklistReqVO {

    @Schema(description = "人脸照片地址")
    @NotNull(message = "人脸照片地址不能为空")
    private String photoUrl;

    @Schema(description = "门店ID")
    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @Schema(description = "备注")
    private String remark;

}
