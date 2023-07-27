package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 设备管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class DeviceInfoBaseVO {

    @Schema(description = "设备sn", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "设备sn不能为空")
    private String deviceSn;

    @Schema(description = "设备类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "设备类型不能为空")
    private Integer type;

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "8726")
    @NotNull(message = "房间id不能为空")
    private Long roomId;

}
