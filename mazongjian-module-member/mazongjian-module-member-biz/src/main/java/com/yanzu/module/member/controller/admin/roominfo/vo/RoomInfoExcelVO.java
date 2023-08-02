package com.yanzu.module.member.controller.admin.roominfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 房间管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class RoomInfoExcelVO {

    @ExcelProperty("房间id")
    private Long roomId;

    @ExcelProperty("房间名称")
    private String roomName;

    @ExcelProperty("门店id")
    private Long storeId;

    @ExcelProperty(value = "房间类型", converter = DictConvert.class)
    @DictFormat("member_room_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer type;

    @ExcelProperty("单价")
    private BigDecimal price;

    @ExcelProperty("房间标签")
    private String label;

    @ExcelProperty("排序位置")
    private Integer sortId;

    @ExcelProperty("禁用开始时间")
    private String banTimeStart;

    @ExcelProperty("禁用结束时间")
    private String banTimeEnd;

    @ExcelProperty("总完成订单数")
    private Integer totalOrderNum;

    @ExcelProperty("总收益")
    private BigDecimal totalMoney;

    @ExcelProperty("状态")
    private Integer status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
