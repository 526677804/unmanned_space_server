package com.yanzu.module.member.controller.admin.member.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 门店微信支付配置创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreWxpayConfigCreateReqVO extends StoreWxpayConfigBaseVO {

    @Schema(description = "证书key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "证书key不能为空")
    private String apiclientKey;

    @Schema(description = "证书cert", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "证书cert不能为空")
    private String apiclientCert;

}
