package com.yanzu.module.member.controller.app.order.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/27 11:29
 */
@Data
@NoArgsConstructor
public class WxPayOrderInfo implements Serializable {

    //随机生成的订单号
    private String orderNo;
    private Long userId;
    //租户id 用于处理不同租户的订单
    private Long tenantId;
    //充值需要这个字段
    private Long storeId;
    //如果是充值  是没有房间id的
    private Long roomId;
    private Date startTime;
    private Date endTime;
    //优惠券信息
    private Long couponId;
    //如果是下单 这个字段是为空的
    private Long ignoreOrderId;
    private int price;
    //是否通宵
    private Boolean nightLong;

    /**
     *
     * @param orderNo
     * @param userId
     * @param tenantId
     * @param storeId
     * @param price
     */
    public WxPayOrderInfo(String orderNo, Long userId, Long tenantId, Long storeId, int price) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.tenantId = tenantId;
        this.storeId = storeId;
        this.price = price;
    }

    /**
     *
     * @param orderNo
     * @param userId
     * @param tenantId
     * @param storeId
     * @param roomId
     * @param startTime
     * @param endTime
     * @param couponId
     * @param ignoreOrderId
     * @param price
     * @param nightLong
     */
    public WxPayOrderInfo(String orderNo, Long userId, Long tenantId, Long storeId, Long roomId, Date startTime, Date endTime, Long couponId, Long ignoreOrderId, int price, Boolean nightLong) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.tenantId = tenantId;
        this.storeId = storeId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.couponId = couponId;
        this.ignoreOrderId = ignoreOrderId;
        this.price = price;
        this.nightLong = nightLong;
    }
}


