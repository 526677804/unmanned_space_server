package com.yanzu.module.member.service.productorder;

import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.productorder.vo.AppSaveOrderReqVo;

public interface ProductOrderService {

    /**
     * 创建订单
     * @param reqVo reqVo
     * @return map
     */
    WxPayOrderRespVO createOrder(AppSaveOrderReqVo reqVo);

    void cancelPay(String orderNo);

}
