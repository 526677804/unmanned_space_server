package com.yanzu.module.member.controller.admin.wxpay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 门店微信支付配置更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreWxpayConfigUpdateReqVO extends StoreWxpayConfigBaseVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17181")
    @NotNull(message = "ID不能为空")
    private Long id;

}
