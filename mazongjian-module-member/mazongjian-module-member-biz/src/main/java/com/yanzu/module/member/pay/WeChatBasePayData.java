package com.yanzu.module.member.pay;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author cv大魔王
 * @version 1.0
 * @description 微信支付基础请求数据对象
 * @date 2022/8/4
 */
@Data
public class WeChatBasePayData {

    /**
     * 商品描述
     */
    private String title;

    /**
     * 商家订单号，对应 out_trade_no
     */
    private String orderId;

    /**
     * 订单金额
     */
    private BigDecimal price;

    /**
     * 回调地址
     */
    private WxNotifyType notify;
}
