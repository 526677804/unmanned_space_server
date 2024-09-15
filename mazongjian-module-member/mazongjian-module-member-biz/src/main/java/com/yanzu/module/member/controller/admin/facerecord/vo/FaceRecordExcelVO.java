package com.yanzu.module.member.controller.admin.facerecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 人脸识别记录 Excel VO
 *
 * @author 超级管理员
 */
@Data
public class FaceRecordExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("识别记录ID")
    private Long faceId;

    @ExcelProperty("人员guid")
    private String admitGuid;

    @ExcelProperty("照片")
    private String photoUrl;

    @ExcelProperty("识别时间")
    private LocalDateTime showTime;

    @ExcelProperty("比对结果")
    private Integer type;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("设备编号")
    private String deviceSn;

}
