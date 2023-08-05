package com.yanzu.module.pay.api.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @PACKAGE_NAME: com.yanzu.module.pay.api.order.dto
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/8/4 15:36
 */
@Data
public class WxPayOrderRespDTO {
    private Long id;
    //@Schema(description = "应用ID")
    private String appId;
    //@Schema(description = "随机字符串")
    private String nonceStr;
    //@Schema(description = "订单详情扩展字符串，原名package")
    private String pkg;
    //@Schema(description = "签名方式")
    private String signType;
    //@Schema(description = "时间戳")
    private String timeStamp;
    //@Schema(description = "签名")
    private String paySign;
    //@Schema(description = "计算出来实际需要支付的价格 单位/元")
    private BigDecimal price;
}
