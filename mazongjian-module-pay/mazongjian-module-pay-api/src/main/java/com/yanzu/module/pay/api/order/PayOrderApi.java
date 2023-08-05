package com.yanzu.module.pay.api.order;

import com.yanzu.module.pay.api.order.dto.PayOrderCreateReqDTO;
import com.yanzu.module.pay.api.order.dto.PayOrderRespDTO;
import com.yanzu.module.pay.api.order.dto.WxPayOrderCreateReqDTO;
import com.yanzu.module.pay.api.order.dto.WxPayOrderRespDTO;

import javax.validation.Valid;

/**
 * 支付单 API 接口
 *
 * @author LeeYan9
 * @since 2022-08-26
 */
public interface PayOrderApi {

    /**
     * 创建支付单
     *
     * @param reqDTO 创建请求
     * @return 支付单编号
     */
    Long createOrder(@Valid PayOrderCreateReqDTO reqDTO);

    WxPayOrderRespDTO createWxOrder(@Valid WxPayOrderCreateReqDTO reqDTO);

    /**
     * 获得支付单
     *
     * @param id 支付单编号
     * @return 支付单
     */
    PayOrderRespDTO getOrder(Long id);

}
