package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.store.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2024/3/25 12:27
 */
@Data
public class AppRoomListVO {

    @Schema(description = "房间id")
    private Long roomId;

    @Schema(description = "门店id")
    private Long storeId;

    @Schema(description = "房间名称")
    private String roomName;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "webhook地址")
    private String orderWebhook;

    private Integer status;
    /**
     * 禁用开始时间
     */
    private String banTimeStart;
    /**
     * 禁用结束时间
     */
    private String banTimeEnd;

    /**
     * 租户ID
     */
    private Long tenantId;

    @Schema(description = "预付费价格")
    private BigDecimal prePrice;

    @Schema(description = "预付费计价分钟")
    private Integer preUnit;

    @Schema(description = "最低消费价格")
    private BigDecimal minCharge;



}
