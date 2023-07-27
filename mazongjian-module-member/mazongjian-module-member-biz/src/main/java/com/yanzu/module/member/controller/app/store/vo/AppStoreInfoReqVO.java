package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "miniapp - 门店信息保存 Response VO")
@Data
@ToString(callSuper = true)
public class AppStoreInfoReqVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13809")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String cityName;

    @Schema(description = "缩略图url")
    private String headImg;

    @Schema(description = "门店风采图片url")
    private String storeEnvImg;

    @Schema(description = "门店公告")
    private String notice;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "wifi信息")
    private String wifiInfo;

    @Schema(description = "客服电话")
    private String kefuPhone;

    @Schema(description = "工作日折扣", example = "25916")
    private Integer workDiscount;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;


}
