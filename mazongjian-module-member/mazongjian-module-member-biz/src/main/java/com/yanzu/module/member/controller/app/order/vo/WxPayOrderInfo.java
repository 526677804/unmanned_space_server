package com.yanzu.module.member.controller.app.order.vo;

import com.yanzu.module.member.enums.AppWxPayTypeEnum;
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
    //套餐信息
    private Long pkgId;
    //如果是下单 这个字段是为空的
    private Long ignoreOrderId;
    private int price;
    //是否通宵
    private Boolean nightLong;
    //回调消息类型 1=下单 2=续费 3=充值 4=套餐购买
    private AppWxPayTypeEnum wxPayTypeEnum;

    /**
     * @param wxPayTypeEnum
     * @param orderNo
     * @param userId
     * @param tenantId
     * @param storeId
     * @param price
     */
    public WxPayOrderInfo(AppWxPayTypeEnum wxPayTypeEnum, String orderNo, Long userId, Long tenantId, Long storeId, int price) {
        this.wxPayTypeEnum = wxPayTypeEnum;
        this.orderNo = orderNo;
        this.userId = userId;
        this.tenantId = tenantId;
        this.storeId = storeId;
        this.price = price;
    }

    /**
     *
     * @param wxPayTypeEnum
     * @param orderNo
     * @param userId
     * @param tenantId
     * @param storeId
     * @param price
     * @param pkgId
     */
    public WxPayOrderInfo(AppWxPayTypeEnum wxPayTypeEnum,String orderNo, Long userId, Long tenantId, Long storeId, int price, Long pkgId) {
        this.wxPayTypeEnum = wxPayTypeEnum;
        this.orderNo = orderNo;
        this.userId = userId;
        this.tenantId = tenantId;
        this.storeId = storeId;
        this.price = price;
        this.pkgId = pkgId;
    }

    /**
     * @param wxPayTypeEnum
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
    public WxPayOrderInfo(AppWxPayTypeEnum wxPayTypeEnum,String orderNo, Long userId, Long tenantId, Long storeId, Long roomId, Date startTime, Date endTime, Long couponId,Long pkgId, Long ignoreOrderId, int price, Boolean nightLong) {
        this.wxPayTypeEnum = wxPayTypeEnum;
        this.orderNo = orderNo;
        this.userId = userId;
        this.tenantId = tenantId;
        this.storeId = storeId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.couponId = couponId;
        this.pkgId = pkgId;
        this.ignoreOrderId = ignoreOrderId;
        this.price = price;
        this.nightLong = nightLong;
    }
}


