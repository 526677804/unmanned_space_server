package com.yanzu.module.member.controller.admin.discountrules.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 充值优惠规则管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class DiscountRulesExcelVO {

    @ExcelProperty("ID")
    private Long discountId;

    @ExcelProperty("支付金额")
    private BigDecimal payMoney;

    @ExcelProperty("赠送金额")
    private BigDecimal giftMoney;

    @ExcelProperty("过期时间")
    private LocalDateTime expriceTime;

    @ExcelProperty("适用门店id")
    private String storeIds;

    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("member_discount_rules_status") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
