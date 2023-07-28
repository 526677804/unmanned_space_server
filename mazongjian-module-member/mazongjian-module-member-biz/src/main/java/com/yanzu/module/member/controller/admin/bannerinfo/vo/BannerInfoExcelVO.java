package com.yanzu.module.member.controller.admin.bannerinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 广告管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class BannerInfoExcelVO {

    @ExcelProperty("编号")
    private Long id;

    @ExcelProperty("图片地址")
    private String imgUrl;

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty("跳转地址/页面路径")
    private String jumpUrl;

    @ExcelProperty("排序")
    private Integer sortId;

    @ExcelProperty(value = "广告类型", converter = DictConvert.class)
    @DictFormat("member_banner_type") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Byte type;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
