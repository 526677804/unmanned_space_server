package com.yanzu.module.member.controller.admin.wxpay.vo;

import lombok.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 门店微信支付配置 Excel VO
 *
 * @author MrGuan
 */
@Data
public class StoreWxpayConfigExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("门店ID")
    private Long storeId;

    @ExcelProperty("小程序id")
    private String appId;

    @ExcelProperty("微信支付商户号")
    private String mchId;


}
