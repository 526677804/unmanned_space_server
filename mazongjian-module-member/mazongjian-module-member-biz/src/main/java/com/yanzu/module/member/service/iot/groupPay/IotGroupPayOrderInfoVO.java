package com.yanzu.module.member.service.iot.groupPay;

import lombok.Data;

import java.util.Date;

@Data
public class IotGroupPayOrderInfoVO {
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 订单key
     */
    private String orderKey;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 订单开始时间
     */
    private Date startTime;
    /**
     * 订单结束时间
     */
    private Date endTime;

    private Integer status;

    private Long tenantId;
}
