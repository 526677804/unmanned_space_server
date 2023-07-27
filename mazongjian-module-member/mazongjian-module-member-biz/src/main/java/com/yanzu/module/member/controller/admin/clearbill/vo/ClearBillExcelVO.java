package com.yanzu.module.member.controller.admin.clearbill.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 保洁账单管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ClearBillExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("用户id")
    private Long userId;

    @ExcelProperty("结算金额")
    private BigDecimal money;

    @ExcelProperty("订单数量")
    private Integer orderNum;

    @ExcelProperty("订单ids")
    private String orderIds;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
