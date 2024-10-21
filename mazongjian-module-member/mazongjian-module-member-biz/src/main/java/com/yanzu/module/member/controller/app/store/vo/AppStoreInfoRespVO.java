package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Schema(description = "miniapp - 门店信息详情 Response VO")
@Data
@ToString(callSuper = true)
public class AppStoreInfoRespVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13809")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String cityName;

    @Schema(description = "缩略图url")
    private String headImg;

    @Schema(description = "门店风采图片url 逗号分隔")
    private String storeEnvImg;

    @Schema(description = "banner url 逗号分隔")
    private String bannerImg;

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
    @Schema(description = "wifi密码")
    private String wifiPwd;
    @Schema(description = "简洁模式")
    private Boolean simpleModel;

    @Schema(description = "客服电话")
    private String kefuPhone;

    @Schema(description = "工作日折扣", example = "25916")
    private Integer workDiscount;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;

    @Schema(description = "订单清洁时间")
    private Integer clearTime;
    @Schema(description = "清洁时开放房间")
    private Boolean clearOpen;
    @Schema(description = "二维码照片")
    private String qrCode;
    @Schema(description = "预约按钮照片")
    private String btnImg;

    @Schema(description = "团购按钮照片")
    private String tgImg;

    @Schema(description = "充值按钮照片")
    private String czImg;

    @Schema(description = "wifi按钮照片")
    private String wifiImg;

    @Schema(description = "切换门店按钮照片")
    private String qhImg;

    @Schema(description = "一键开门按钮照片")
    private String openImg;

    @Schema(description = "客服按钮照片")
    private String kfImg;

    @Schema(description = "显示通宵价格")
    private Boolean showTxPrice;
    @Schema(description = "通宵开始小时")
    private Integer txStartHour;
    @Schema(description = "通宵小时时长")
    private Integer txHour;
    @Schema(description = "延时关灯")
    private Boolean delayLight;
    @Schema(description = "启用工作日价格")
    private Boolean workPrice;
    @Schema(description = "订单门禁常开")
    private Boolean orderDoorOpen;
    @Schema(description = "保洁任意开门")
    private Boolean clearOpenDoor;


    @Schema(description = "webhook")
    private String orderWebhook;
}
