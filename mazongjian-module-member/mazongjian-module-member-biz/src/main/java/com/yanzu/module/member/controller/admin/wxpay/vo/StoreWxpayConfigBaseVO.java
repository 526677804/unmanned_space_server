package com.yanzu.module.member.controller.admin.wxpay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * 门店微信支付配置 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class StoreWxpayConfigBaseVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7352")
    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @Schema(description = "APPID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7352")
    @NotNull(message = "APPID不能为空")
    private String appId;

    @Schema(description = "微信支付商户号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23018")
    @NotNull(message = "微信支付商户号不能为空")
    private String mchId;

    @Schema(description = "服务商支付模式", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Boolean serviceModel;

    @Schema(description = "是否分账", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Boolean split;

    @Schema(description = "分账比例", requiredMode = Schema.RequiredMode.REQUIRED, example = "23018")
    @Max(value = 30,message = "最大比例不能超过30")
    private Integer splitProp;

    @Schema(description = "支付密钥", requiredMode = Schema.RequiredMode.REQUIRED, example = "23018")
    private String mchKey;

    @Schema(description = "p12证书", requiredMode = Schema.RequiredMode.REQUIRED, example = "23018")
    private String p12;





}
