package com.yanzu.module.member.controller.admin.deviceinfo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 设备管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class DeviceInfoExcelVO {

    @ExcelProperty("设备id")
    private Long deviceId;

    @ExcelProperty("设备sn")
    private String deviceSn;

    @ExcelProperty(value = "设备类型", converter = DictConvert.class)
    @DictFormat("member_device_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer type;

    @ExcelProperty("房间id")
    private Long roomId;

    @ExcelProperty("门店id")
    private Long storeId;

    @ExcelProperty("状态")
    private Integer status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
