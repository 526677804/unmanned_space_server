package com.yanzu.module.member.controller.admin.storeinfo.vo;

import com.yanzu.framework.common.validation.Mobile;
import lombok.*;

import java.util.*;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 门店管理创建 Request VO")
@Data
@ToString(callSuper = true)
public class StoreInfoCreateReqVO {

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotNull(message = "门店名称不能为空")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotNull(message = "城市名称不能为空")
    private String cityName;

    @Schema(description = "门店介绍")
    private String content;

    @Schema(description = "门店公告")
    private String notice;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "详细地址")
    @NotNull(message = "详细地址不能为空")
    private String address;

    @Schema(description = "门店状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "门店状态不能为空")
    private Integer status;

    @Schema(description = "wifi信息")
    private String wifiInfo;

    @Schema(description = "wifi密码")
    private String wifiPwd;

    @Schema(description = "客服电话")
    private String kefuPhone;

    @Schema(description = "订单通知webhook")
    private String orderWebhook;

    @Schema(description = "美团店铺uuid")
    private String meituanOpenShopUuid;

    @Schema(description = "抖音poiId")
    private String douyinPoiId;

    @Schema(description = "房间数量")
    @NotNull(message = "房间数量不能为空")
    @Min(value = 1,message = "房间数量至少是1")
    private Integer roomNum;

    @Schema(description = "超管手机号", example = "266")
    @NotNull(message = "超管手机号能为空")
    @Mobile
    private String mobile;
}
