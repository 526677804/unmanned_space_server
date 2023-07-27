package com.yanzu.module.member.controller.admin.clearinfo.vo;

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
 * 保洁信息管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ClearInfoExcelVO {

    @ExcelProperty("清洁记录id")
    private Long clearId;

    @ExcelProperty("订单id")
    private Long orderId;

    @ExcelProperty("订单编号")
    private String orderNo;

    @ExcelProperty("清洁图片")
    private String imgs;

    @ExcelProperty("用户id")
    private Long userId;

    @ExcelProperty("投诉的照片")
    private String complaintImgs;

    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @ExcelProperty("结算时间")
    private LocalDateTime settlementTime;

    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("member_clear_info_status") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
