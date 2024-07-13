package com.yanzu.module.member.service.iot.device;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class IotDeviceConfigWifiReqVO extends IotDeviceBaseVO<IotDeviceConfigWifiReqVO> {

    @Schema(description = "wifi名称")
    @NotNull
    private String ssid;


    @Schema(description = "wifi密码")
    @NotNull
    @Length(min = 8, max = 32, message = "wifi密码长度不正确")
    private String passwd;


}