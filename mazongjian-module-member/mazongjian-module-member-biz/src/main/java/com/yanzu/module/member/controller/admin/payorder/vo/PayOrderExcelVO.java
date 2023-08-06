package com.yanzu.module.member.controller.admin.payorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 支付订单 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class PayOrderExcelVO {

    @ExcelProperty("订单编号")
    private Long id;

    @ExcelProperty("用户编号")
    private Long userId;

    @ExcelProperty("订单编号")
    private String orderNo;

    @ExcelProperty("订单内容")
    private String orderDesc;

    @ExcelProperty("价格")
    private Integer price;

    @ExcelProperty(value = "是否已支付", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Boolean payStatus;

    @ExcelProperty("支付订单编号")
    private String payOrderNo;

    @ExcelProperty("订单支付时间")
    private LocalDateTime payTime;

    @ExcelProperty("退款订单编号")
    private String payRefundNo;

    @ExcelProperty("退款金额")
    private Integer refundPrice;

    @ExcelProperty("退款时间")
    private LocalDateTime refundTime;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
