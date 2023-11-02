package com.yanzu.module.member.controller.app.order.vo;

import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/27 11:29
 */
@Data
public class WxPayOrderInfo implements Serializable {

    //随机生成的订单号
    private String orderNo;
    private Long userId;
    //充值需要这个字段
    private Long storeId;
    //如果是充值  是没有房间id的
    private Long roomId;
    private Date startTime;
    private Date endTime;
    //优惠券信息
    private CouponInfoDO couponInfoDO;
    //如果是下单 这个字段是为空的
    private Long ignoreOrderId;
    private int price;

    /**
     * 充值的构造函数
     * @param orderNo
     * @param userId
     * @param storeId
     * @param price
     */
    public WxPayOrderInfo(String orderNo, Long userId, Long storeId, int price) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.storeId = storeId;
        this.price = price;
    }

    /**
     *
     * @param orderNo
     * @param userId
     * @param storeId
     * @param roomId
     * @param startTime
     * @param endTime
     * @param couponInfoDO
     * @param ignoreOrderId
     * @param price
     */
    public WxPayOrderInfo(String orderNo, Long userId, Long storeId, Long roomId, Date startTime, Date endTime, CouponInfoDO couponInfoDO, Long ignoreOrderId, int price) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.storeId = storeId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.couponInfoDO = couponInfoDO;
        this.ignoreOrderId = ignoreOrderId;
        this.price = price;
    }
}


