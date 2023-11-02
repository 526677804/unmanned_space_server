package com.yanzu.module.member.controller.admin.groupPay.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 团购支付信息 Excel VO
 *
 * @author MrGuan
 */
@Data
public class GroupPayInfoExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("团购券名称")
    private String groupName;

    @ExcelProperty("团购券编码")
    private String groupNo;

    @ExcelProperty("价格")
    private BigDecimal groupPayPrice;

    @ExcelProperty(value = "团购券类型", converter = DictConvert.class)
    @DictFormat("member_group_no_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer groupPayType;

    @ExcelProperty("门店")
    private Long storeId;

    @ExcelProperty("订单")
    private Long orderId;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
