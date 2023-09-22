package com.yanzu.module.member.controller.admin.storeinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 门店管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreInfoRespVO extends StoreInfoBaseVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2995")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String cityName;

    @Schema(description = "门店公告")
    private String notice;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "门店状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "wifi信息")
    private String wifiInfo;

    @Schema(description = "客服电话")
    private String kefuPhone;

    @Schema(description = "订单通知webhook")
    private String orderWebhook;

    @Schema(description = "组局通知webhook")
    private String gameWebhook;

    @Schema(description = "房间数量")
    private Integer roomNum;

    @Schema(description = "总收入")
    private BigDecimal totalMoney;

    @Schema(description = "已提现")
    private BigDecimal totalWithdrawal;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
