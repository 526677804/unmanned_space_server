package com.yanzu.module.member.controller.admin.deviceuseinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 设备使用记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceUseInfoUpdateReqVO extends DeviceUseInfoBaseVO {

}
