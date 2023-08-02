package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 设备管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceInfoUpdateReqVO extends DeviceInfoBaseVO {

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "9449")
    @NotNull(message = "设备id不能为空")
    private Long deviceId;

}
