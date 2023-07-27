package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "miniapp - 门店管理列表 Response VO")
@Data
@ToString(callSuper = true)
public class AppStoreAdminRespVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13809")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String storeName;

    @Schema(description = "缩略图url")
    private String headImg;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "门店状态 值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "房间数量")
    private Integer roomNum;

    @Schema(description = "设备总数量")
    private Integer deviceNum;

    @Schema(description = "设备在线数量")
    private Integer deviceOnlineNum;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;


}
