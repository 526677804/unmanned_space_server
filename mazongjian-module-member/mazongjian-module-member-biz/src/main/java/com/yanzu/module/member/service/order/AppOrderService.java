package com.yanzu.module.member.service.order;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.order.vo.*;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.module.member.dal.dataobject.pkginfo.PkgInfoDO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AppOrderService {

    WxPayOrderRespVO preOrder(Long userId, Integer payType, Long roomId, Date startTime, Date endTime, CouponInfoDO couponInfoDO, PkgInfoDO pkgInfoDO, Long ignoreOrderId, boolean tongxiao, boolean wxpay);

    BigDecimal mathPrice(BigDecimal price, BigDecimal deposit, BigDecimal workPrice, Boolean enableWorkPrice, BigDecimal tongxiaoPrice, Integer txHour, Date startTime, Date endTime, Boolean nightLong, CouponInfoDO couponInfoDO, PkgInfoDO pkgInfoDO);

    Long save(OrderSaveReqVO reqVO);

    void renew(OrderRenewalReqVO reqVO);

    PageResult<OrderListRespVO> getOrderPage(OrderPageReqVO reqVO);

    OrderInfoAppRespVO getOrderInfo(Long orderId, String orderKey);

    String getRoomImgs(Long roomId);

    void changeRoom(Long orderId, Long roomId);


    void cancelOrder(Long orderId);

    void startOrder(Long orderId);

    void executeOrderJob();

    boolean queryWxOrder(String orderNo);

    List<AppDiscountRulesRespVO> getDiscountRules(Long storeId);

    void executeMeituanRefreshTokenJob();

    void openRoomDoor(String orderKey);

    void openStoreDoor(String orderKey);

    int countByUserAndStoreId(Long userId, Long storeId);

    void lockWxOrder(OrderPreReqVO reqVO);


    void flushRoomStatus(Long roomId);

    int countNewUserByStoreId(Long userId, Long storeId);

    OrderInfoAppRespVO getOrderByRoomId(Long roomId);

    void closeOrder(Long orderId);

    String preGroupNo(PreGroupNoReqVO reqVO);

    void controlKT(ControlKTReqVO reqVO);


}
