package com.yanzu.module.member.controller.admin.faceblacklist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 人脸黑名单 Excel VO
 *
 * @author 超级管理员
 */
@Data
public class FaceBlacklistExcelVO {

    @ExcelProperty("ID")
    private Long blacklistId;

    @ExcelProperty("门店")
    private Long storeId;

    @ExcelProperty("照片")
    private String photoUrl;

    @ExcelProperty("人员guid")
    private String admitGuid;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
