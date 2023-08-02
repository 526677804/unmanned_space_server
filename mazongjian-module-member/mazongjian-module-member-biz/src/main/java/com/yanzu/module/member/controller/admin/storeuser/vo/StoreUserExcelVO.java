package com.yanzu.module.member.controller.admin.storeuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 门店用户管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class StoreUserExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("门店id")
    private Long storeId;

    @ExcelProperty("用户id")
    private Long userId;

    @ExcelProperty("真实姓名")
    private String name;

    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("member_store_user_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer type;

    @ExcelProperty("状态")
    private Integer status;

    @ExcelProperty("赠送余额")
    private BigDecimal giftBalance;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
