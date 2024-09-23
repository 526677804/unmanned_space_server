package com.yanzu.module.member.controller.app.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Schema(description = "miapp - 门店管理 Response VO")
@Data
@ToString(callSuper = true)
public class AppIndexStoreInfoRespVO  {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13809")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String cityName;

    @Schema(description = "距离")
    private BigDecimal distance;

    @Schema(description = "门店富文本详情")
    private String content;

    @Schema(description = "缩略图url")
    private String headImg;

    @Schema(description = "门店环境/门店风采照片url 逗号分隔")
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

    @Schema(description = "工作日折扣 1-100", example = "8")
    private Integer workDiscount;

    @Schema(description = "门店优惠信息")
    private List<String> discountRules;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;

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

    @Schema(description = "订单清洁时间")
    private Integer clearTime;

    @Schema(description = "清洁时开放房间")
    private Boolean clearOpen;

    @Schema(description = "门店房间类别列表")
    private List<Integer> roomClassList;
}
