package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class AppFaceRecordRespVO {


    @Schema(description = "id")
    private Long id;

    @Schema(description = "识别记录ID")
    private String faceId;

    @Schema(description = "人员id")
    private String admitGuid;

    @Schema(description = "人员照片")
    private String photoUrl;

    @Schema(description = "识别时间")
    private Date showTime;

    @Schema(description = "结果类型")
    private Integer type;

    @Schema(description = "设备编号")
    private String deviceSn;

    @Schema(description = "门店id")
    private Long storeId;
}
