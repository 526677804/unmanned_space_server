package com.yanzu.module.member.controller.admin.member.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 门店微信支付配置 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class StoreWxpayConfigBaseVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7352")
    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @Schema(description = "小程序id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25462")
    @NotNull(message = "小程序id不能为空")
    private String appId;

    @Schema(description = "微信支付商户号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23018")
    @NotNull(message = "微信支付商户号不能为空")
    private String mchId;

    @Schema(description = "微信支付商户密钥", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "微信支付商户密钥不能为空")
    private String mchKey;

}
