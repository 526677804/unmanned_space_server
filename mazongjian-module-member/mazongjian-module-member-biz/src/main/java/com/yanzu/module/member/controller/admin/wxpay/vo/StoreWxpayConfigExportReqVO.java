package com.yanzu.module.member.controller.admin.wxpay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 门店微信支付配置 Excel 导出 Request VO，参数和 StoreWxpayConfigPageReqVO 是一致的")
@Data
public class StoreWxpayConfigExportReqVO {

    @Schema(description = "门店ID", example = "7352")
    private Long storeId;

    @Schema(description = "是否分账", example = "1")
    private Boolean split;

    @Schema(description = "服务商支付模式", example = "1")
    private Boolean serviceModel;

    @Schema(description = "微信支付商户号", example = "23018")
    private String mchId;


}
