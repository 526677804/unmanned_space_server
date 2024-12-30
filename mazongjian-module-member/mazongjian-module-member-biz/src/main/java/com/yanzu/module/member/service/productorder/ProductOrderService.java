package com.yanzu.module.member.service.productorder;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.productorder.vo.*;

import java.util.List;

public interface ProductOrderService {

    /**
     * 创建订单
     * @param reqVo reqVo
     * @return map
     */
    WxPayOrderRespVO createOrder(AppSaveOrderReqVO reqVo);

    void cancelPay(AppCancelPayReqVO reqVo, boolean isAdmin);

    PageResult<AppUserOrderPageRespVO> userOrderByPage(AppUserOrderPageReqVO reqVo);

    List<AppHaveOrderStoreRespVO> selectHaveOrderStore();

    WxPayOrderRespVO pay(Long orderId);

    // 商家经营管理调用
    PageResult<AppUserOrderPageRespVO> managerProductOrder(AppUserOrderPageReqVO reqVo);

    // 商家完成订单
    void finishOrder(Long id);

    AppUserOrderPageRespVO orderInfo(Long orderId);

    String getPhone(Long orderId);

}
