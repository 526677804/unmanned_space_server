package com.yanzu.module.member.service.order;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.order.vo.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AppOrderService {

    BigDecimal preOrder(Long roomId, Date startTime, Date endTime, Long couponId, Long ignoreOrderId);

    BigDecimal mathPrice(BigDecimal price, Date startTime, Date endTime, Long couponId);

    void save(OrderSaveReqVO reqVO);

    void renew(OrderRenewalReqVO reqVO);

    PageResult<OrderListRespVO> getOrderPage(OrderPageReqVO reqVO);

    OrderInfoAppRespVO getOrderInfo(Long orderId);

    String getRoomImgs(Long roomId);

    void changeRoom(Long orderId, Long roomId);


    void cancelOrder(Long orderId);

    void startOrder(Long orderId);
}
