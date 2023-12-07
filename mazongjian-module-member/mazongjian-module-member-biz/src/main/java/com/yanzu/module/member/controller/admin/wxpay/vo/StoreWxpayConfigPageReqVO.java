package com.yanzu.module.member.controller.admin.wxpay.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 门店微信支付配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreWxpayConfigPageReqVO extends PageParam {

    @Schema(description = "门店ID", example = "7352")
    private Long storeId;

    @Schema(description = "小程序id", example = "25462")
    private String appId;

    @Schema(description = "微信支付商户号", example = "23018")
    private String mchId;


}
