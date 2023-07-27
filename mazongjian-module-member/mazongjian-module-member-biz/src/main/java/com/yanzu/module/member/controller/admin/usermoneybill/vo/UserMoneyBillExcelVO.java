package com.yanzu.module.member.controller.admin.usermoneybill.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 用户账单明细 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class UserMoneyBillExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("用户ID")
    private Long userId;

    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("member_user_money_bill_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer type;

    @ExcelProperty("金额")
    private BigDecimal money;

    @ExcelProperty(value = "金额类型", converter = DictConvert.class)
    @DictFormat("member_user_money_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer moneyType;

    @ExcelProperty("当时总账户余额")
    private BigDecimal totalMoney;

    @ExcelProperty("当时总赠送余额")
    private BigDecimal totalGiftMoney;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
