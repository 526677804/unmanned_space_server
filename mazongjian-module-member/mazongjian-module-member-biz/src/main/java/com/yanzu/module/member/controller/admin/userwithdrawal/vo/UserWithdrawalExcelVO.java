package com.yanzu.module.member.controller.admin.userwithdrawal.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 用户提现 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class UserWithdrawalExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("编号")
    private String no;

    @ExcelProperty("用户Id")
    private Long userId;

    @ExcelProperty("提现金额")
    private BigDecimal money;

    @ExcelProperty("完成时间")
    private LocalDateTime finishTime;

    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("member_user_withdrawal") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Boolean status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
