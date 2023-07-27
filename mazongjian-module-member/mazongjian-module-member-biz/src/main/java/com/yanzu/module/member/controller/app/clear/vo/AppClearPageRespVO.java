package com.yanzu.module.member.controller.app.clear.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "miniapp - 保洁信息 Response VO")
@Data
@ToString(callSuper = true)
public class AppClearPageRespVO {

    @Schema(description = "清洁记录id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13416")
    private Long clearId;

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "30333")
    private Long orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "门店Id")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "房间名称")
    private String roomName;

    @Schema(description = "房间类型")
    private Integer roomType;

    @Schema(description = "订单结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime orderEndTime;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "状态 值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;


}
