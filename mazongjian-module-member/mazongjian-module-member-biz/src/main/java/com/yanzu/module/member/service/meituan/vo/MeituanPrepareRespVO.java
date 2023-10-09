package com.yanzu.module.member.service.meituan.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.douyin.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/9 11:45
 */
@Data
public class MeituanPrepareRespVO {


    //用户实际支付价格  单位 元
    private BigDecimal payAmount;
    //团购名称
    private String title;
    //套餐id，退款的时候要用
    private String dealId;
}
