package com.yanzu.module.member.controller.admin.member.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 门店微信支付配置Page Response VO")
@Data
@ToString(callSuper = true)
public class StoreWxpayConfigPageRespVO  {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17181")
    private Long id;

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7352")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "7352")
    private String storeName;

    @Schema(description = "小程序id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25462")
    private String appId;

    @Schema(description = "微信支付商户号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23018")
    private String mchId;

}
