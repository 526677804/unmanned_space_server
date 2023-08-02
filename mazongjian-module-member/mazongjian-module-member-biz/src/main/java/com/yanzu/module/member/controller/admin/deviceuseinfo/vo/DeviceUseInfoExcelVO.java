package com.yanzu.module.member.controller.admin.deviceuseinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 设备使用记录 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class DeviceUseInfoExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("用户id")
    private Long userId;

    @ExcelProperty("门店id")
    private Long storeId;

    @ExcelProperty("房间id")
    private Long roomId;

    @ExcelProperty("命令")
    private String cmd;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
