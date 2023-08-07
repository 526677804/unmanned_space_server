package com.yanzu.module.member.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信回调地址，根据自己的项目来，不要直接照搬
 */
@Getter
@AllArgsConstructor
public enum WxNotifyType {

	/**
	 * 支付通知
	 */

	MINIAPP_PAY_NOTIFY("/wxpay/update"),


	/**
	 * 退款结果通知
	 */
	REFUND_NOTIFY("/wxpay/urefunded");

	/**
	 * 类型
	 */
	private final String type;
}
