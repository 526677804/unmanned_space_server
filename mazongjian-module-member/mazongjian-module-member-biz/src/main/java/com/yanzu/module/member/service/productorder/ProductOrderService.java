package com.yanzu.module.member.service.productorder;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.productorder.vo.AppHaveOrderStoreRespVo;
import com.yanzu.module.member.controller.app.productorder.vo.AppSaveOrderReqVo;
import com.yanzu.module.member.controller.app.productorder.vo.AppUserOrderPageReqVo;
import com.yanzu.module.member.controller.app.productorder.vo.AppUserOrderPageRespVo;

import java.util.List;

public interface ProductOrderService {

    /**
     * 创建订单
     * @param reqVo reqVo
     * @return map
     */
    WxPayOrderRespVO createOrder(AppSaveOrderReqVo reqVo);

    void cancelPay(String orderNo);

    PageResult<AppUserOrderPageRespVo> userOrderByPage(AppUserOrderPageReqVo reqVo);

    List<AppHaveOrderStoreRespVo> selectHaveOrderStore();

    WxPayOrderRespVO pay(Long orderId);

    // 商家经营管理调用
    PageResult<AppUserOrderPageRespVo> managerProductOrder(AppUserOrderPageReqVo reqVo);

    // 商家完成订单
    void finishOrder(Long id);

    AppUserOrderPageRespVo orderInfo(Long orderId);

    String getPhone(Long orderId);

}
