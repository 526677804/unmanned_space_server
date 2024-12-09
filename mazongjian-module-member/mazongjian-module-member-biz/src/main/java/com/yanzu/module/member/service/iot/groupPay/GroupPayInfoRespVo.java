package com.yanzu.module.member.service.iot.groupPay;

import lombok.Data;

@Data
public class GroupPayInfoRespVo {

    private Integer storeId;

    private String storeName;

    private String groupPayType;

    private String ticketNo;

    private String ticketName;

    private String ticketInfo;

    private String ticketData;

    private String shopId;

    private Integer payPrice;

    private Integer status;

    private String refundTime;

    private String createTime;
}
