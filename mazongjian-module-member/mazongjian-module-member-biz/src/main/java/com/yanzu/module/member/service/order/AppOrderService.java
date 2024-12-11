package com.yanzu.module.member.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.meituanreserve.vo.MeiTuanReserveReqVo;
import com.yanzu.module.member.controller.app.order.vo.*;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.module.member.dal.dataobject.pkginfo.PkgInfoDO;
import com.yanzu.module.member.service.order.vo.GroupPayTimeReqVo;

import javax.servlet.http.HttpServletResponse;
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

    AppGroupNoInfoRespVO preGroupNo(PreGroupNoReqVO reqVO);

    void controlKT(ControlKTReqVO reqVO);

    /**
     * 开始预定
     *
     * @param storeId
     * @param message
     * @param response
     * @return
     */
    void startBooking(Long storeId , String message, HttpServletResponse response);

    /**
     * 预定结果同步
     *
     * @param storeId
     * @param message
     * @param response
     * @return
     */
    void resultSynchronization(Long storeId , String message, HttpServletResponse response);

    /**
     * 取消预定
     *
     * @param storeId
     * @param message
     * @param response
     * @return
     */
    void cancelReserve(Long storeId , String message, HttpServletResponse response);

    /**
     * 核销状态查询
     *
     * @param storeId
     * @param message
     * @param response
     * @return
     */
    void verificationStatus(Long storeId , String message, HttpServletResponse response);

    String getLockPwd(String orderKey);

    Integer getGroupPayTime(GroupPayTimeReqVo reqVo);

}
