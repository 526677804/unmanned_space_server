package com.yanzu.module.member.controller.app.order.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderListJobVO {

    private Long orderId;
    private String orderNo;
    private Long storeId;
    private Long roomId;
    private Long userId;
    private Date startTime;
    private Date endTime;
    private BigDecimal deposit;
    private Integer status;
    private Integer roomClass;
    private Boolean jumpClear;



}
