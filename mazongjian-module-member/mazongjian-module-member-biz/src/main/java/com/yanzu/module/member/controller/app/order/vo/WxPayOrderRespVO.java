package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/8/3 17:07
 */
@Schema(description = "miniapp - 微信支付，下单接口 Response VO")
@Data
@ToString(callSuper = true)
public class WxPayOrderRespVO {

    @Schema(description = "商户 订单号")
    private String orderNo;
    @Schema(description = "应用ID")
    private String appId;
    @Schema(description = "随机字符串")
    private String nonceStr;
    @Schema(description = "订单详情扩展字符串，原名package")
    private String pkg;
    @Schema(description = "签名方式")
    private String signType;
    @Schema(description = "时间戳")
    private String timeStamp;
    @Schema(description = "签名")
    private String paySign;
    @Schema(description = "计算出来实际需要支付的价格 单位/分")
    private Integer price;
    @Schema(description = "实际需支付金额 单位/分")
    private Integer payPrice;

}
