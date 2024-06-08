package com.yanzu.module.member.controller.app.store.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "miniapp - 房间管理列表 Response VO")
@Data
@ToString(callSuper = true)
public class AppRoomListRespVO {

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16599")
    private Long roomId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String roomName;

    @Schema(description = "房间类别", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer roomClass;

    @Schema(description = "密码锁data 用于离线蓝牙开锁")
    private String lockData;

    @Schema(description = "网关id 用于远程开锁")
    private Long gatewayId;

    @Schema(description = "房间小程序码")
    private String qrCode;

    @Schema(description = "房间续费码")
    private String renewCode;

    @Schema(description = "房间类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "20571")
    private BigDecimal price;

    @Schema(description = "房间标签 逗号分隔")
    private String label;

    @Schema(description = "禁用开始时间")
    private String banTimeStart;

    @Schema(description = "禁用结束时间")
    private String banTimeEnd;

    @Schema(description = "总完成订单数")
    private Integer totalOrderNum;

    @Schema(description = "总收益")
    private BigDecimal totalMoney;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "订单结束/预定时间", example = "2")
    private Date orderTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;
}
