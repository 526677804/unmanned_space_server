package com.yanzu.module.member.controller.admin.member.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

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
