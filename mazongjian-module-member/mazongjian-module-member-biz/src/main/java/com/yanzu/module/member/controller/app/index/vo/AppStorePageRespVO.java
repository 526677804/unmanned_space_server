package com.yanzu.module.member.controller.app.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.index.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 14:18
 */
@Schema(description = "miniapp - 首页门店列表VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppStorePageRespVO {

    @Schema(description = "门店id")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "城市名称")
    private String cityName;

    @Schema(description = "缩略图url")
    private String headImg;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "距离")
    private BigDecimal distance;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "客服电话")
    private String kefuPhone;

    @Schema(description = "门店状态")
    private Integer status;

    @Schema(description = "简洁模式")
    private Boolean simpleModel;

    @Schema(description = "充值优惠信息")
    private List<String> discountRules;

    @Schema(description = "空闲房间数量")
    private Long freeRoomNum;

    @Schema(description = "预约时间")
    private String subscribeTime;
}
