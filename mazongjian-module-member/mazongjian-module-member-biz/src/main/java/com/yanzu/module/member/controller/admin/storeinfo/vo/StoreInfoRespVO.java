package com.yanzu.module.member.controller.admin.storeinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 门店管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreInfoRespVO extends StoreInfoBaseVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13809")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String cityName;

    @Schema(description = "门店富文本详情")
    private String content;

    @Schema(description = "缩略图url")
    private String headImg;

    @Schema(description = "门店公告")
    private String notice;

    @Schema(description = "纬度")
     private Double lat;

    @Schema(description = "经度")
     private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "门店状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "wifi信息")
    private String wifiInfo;

    @Schema(description = "房间标签")
    private String label;

    @Schema(description = "工作日折扣", example = "25916")
    private Integer workDiscount;

    @Schema(description = "大门门禁sn")
    private String gateDeviceSn;

    @Schema(description = "大众点评key")
    private String dianpinKey;

    @Schema(description = "美团key")
    private String meituanKey;

    @Schema(description = "抖音key")
    private String douyinKey;

    @Schema(description = "房间数量")
    private Integer roomNum;

    @Schema(description = "总收入")
    private BigDecimal totalMoney;

    @Schema(description = "已提现")
    private BigDecimal totalWithdrawal;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
