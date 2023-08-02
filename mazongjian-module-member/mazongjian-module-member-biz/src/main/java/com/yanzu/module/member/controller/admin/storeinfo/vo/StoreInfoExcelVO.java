package com.yanzu.module.member.controller.admin.storeinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 门店管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class StoreInfoExcelVO {

    @ExcelProperty("门店ID")
    private Long storeId;

    @ExcelProperty("门店名称")
    private String storeName;

    @ExcelProperty("城市名称")
    private String cityName;

    @ExcelProperty("门店公告")
    private String notice;

    @ExcelProperty("纬度")
    private Double lat;

    @ExcelProperty("经度")
    private Double lon;

    @ExcelProperty("详细地址")
    private String address;

    @ExcelProperty("门店状态")
    private Integer status;

    @ExcelProperty("wifi信息")
    private String wifiInfo;

    @ExcelProperty("客服电话")
    private String kefuPhone;

    @ExcelProperty("大众点评key")
    private String dianpinKey;

    @ExcelProperty("美团key")
    private String meituanKey;

    @ExcelProperty("抖音key")
    private String douyinKey;

    @ExcelProperty("房间数量")
    private Integer roomNum;

    @ExcelProperty("总收入")
    private BigDecimal totalMoney;

    @ExcelProperty("已提现")
    private BigDecimal totalWithdrawal;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
