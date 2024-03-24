package com.yanzu.module.member.service.order;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.order.vo.*;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AppOrderService {

    WxPayOrderRespVO preOrder(Long roomId, Date startTime, Date endTime, CouponInfoDO couponInfoDO, Long ignoreOrderId, boolean tongxiao, boolean wxpay);

    BigDecimal mathPrice(BigDecimal price,BigDecimal workPrice, BigDecimal tongxiaoPrice,Date startTime, Date endTime,Boolean nightLong, CouponInfoDO couponInfoDO);

    Long save(OrderSaveReqVO reqVO);

    void renew(OrderRenewalReqVO reqVO);

    PageResult<OrderListRespVO> getOrderPage(OrderPageReqVO reqVO);

    OrderInfoAppRespVO getOrderInfo(Long orderId,String orderKey);

    String getRoomImgs(Long roomId);

    void changeRoom(Long orderId, Long roomId);


    void cancelOrder(Long orderId);

    void startOrder(Long orderId);

    void executeOrderJob();

    boolean queryWxOrder(String orderNo);

    List<AppDiscountRulesRespVO> getDiscountRules(Long storeId);

    void executeMeituanRefreshTokenJob();

    void openRoomDoor(Long orderId);

    void openStoreDoor(Long orderId);



//    void closeOrder(Long orderId);
}
