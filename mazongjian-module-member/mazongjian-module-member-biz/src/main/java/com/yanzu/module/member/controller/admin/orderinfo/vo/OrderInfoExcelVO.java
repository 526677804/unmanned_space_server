package com.yanzu.module.member.controller.admin.orderinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 订单管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class OrderInfoExcelVO {

    @ExcelProperty("订单id")
    private Long orderId;

    @ExcelProperty("订单编号")
    private String orderNo;

    @ExcelProperty("房间id")
    private Long roomId;

    @ExcelProperty("用户id")
    private Long userId;

    @ExcelProperty("订单开始时间")
    private LocalDateTime startTime;

    @ExcelProperty("订单结束时间")
    private LocalDateTime endTime;

    @ExcelProperty("订单价格")
    private BigDecimal price;

    @ExcelProperty("工作日折扣")
    private Integer workDiscount;

    @ExcelProperty("实际支付价格")
    private BigDecimal payPrice;

    @ExcelProperty("退款价格")
    private BigDecimal refundPrice;

    @ExcelProperty(value = "支付方式", converter = DictConvert.class)
    @DictFormat("member_order_pay_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer payType;

    @ExcelProperty("团购券码")
    private String groupPayNo;

    @ExcelProperty("优惠券Id")
    private Long couponId;

    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("member_order_status") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
