package com.yanzu.module.member.controller.admin.couponinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 优惠券管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class CouponInfoExcelVO {

    @ExcelProperty("优惠券ID")
    private Long couponId;

    @ExcelProperty("持有用户")
    private String userId;

    @ExcelProperty("创建用户")
    private String createUserId;

    @ExcelProperty("过期时间")
    private LocalDateTime expriceTime;

    @ExcelProperty("优惠券名称")
    private String couponName;

    @ExcelProperty("使用门槛")
    private BigDecimal minUsePrice;

    @ExcelProperty("优惠券面额")
    private BigDecimal price;

    @ExcelProperty("使用门店")
    private Long storeId;

    @ExcelProperty("使用房间")
    private Long roomId;

    @ExcelProperty(value = "优惠券类型", converter = DictConvert.class)
    @DictFormat("member_coupon_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer type;

    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("member_coupon_status") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
