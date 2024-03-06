package com.yanzu.module.member.controller.admin.franchiseinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 加盟信息 Excel VO
 *
 * @author yanzu
 */
@Data
public class FranchiseInfoExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("城市名称")
    private String city;

    @ExcelProperty("联系人姓名")
    private String contactName;

    @ExcelProperty("联系人电话")
    private String contactPhone;

    @ExcelProperty("留言")
    private String message;

    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("member_franchise_status") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
