package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(description = "miniapp - 门店信息保存 Response VO")
@Data
@ToString(callSuper = true)
public class AppStoreInfoReqVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13809")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotNull(message = "门店名称不能为空")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotNull(message = "城市名称不能为空")
    private String cityName;

    @Schema(description = "缩略图url")
    @NotNull(message = "缩略图url不能为空")
    private String headImg;

    @Schema(description = "门店风采图片url")
    @NotNull(message = "门店风采图片url不能为空")
    private String storeEnvImg;

    @Schema(description = "门店公告")
    @NotNull(message = "门店公告不能为空")
    private String notice;

    @Schema(description = "纬度")
    @NotNull(message = "纬度不能为空")
    private Double lat;

    @Schema(description = "经度")
    @NotNull(message = "经度不能为空")
    private Double lon;

    @Schema(description = "详细地址")
    @NotNull(message = "详细地址不能为空")
    private String address;

    @Schema(description = "wifi信息")
    @NotNull(message = "wifi信息不能为空")
    private String wifiInfo;

    @Schema(description = "客服电话")
    @NotNull(message = "客服电话不能为空")
    private String kefuPhone;

//    @Schema(description = "工作日折扣", example = "25916")
//    @Max(value = 100, message = "工作日折扣不能大于100")
//    @Min(value = 1, message = "工作日折扣不能小于1")
//    private Integer workDiscount;


}
