package com.yanzu.module.member.controller.admin.roominfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 房间管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoomInfoRespVO extends RoomInfoBaseVO {

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16599")
    private Long roomId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String roomName;

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14069")
    private Long storeId;

    @Schema(description = "房间类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "20571")
    private BigDecimal price;

    @Schema(description = "房间标签")
    private String label;

    @Schema(description = "房间照片")
    private String imageUrls;

    @Schema(description = "排序位置", example = "5068")
    private Integer sortId;

    @Schema(description = "禁用开始时间")
    private LocalDateTime banTimeStart;

    @Schema(description = "禁用结束时间")
    private LocalDateTime banTimeEnd;

    @Schema(description = "总完成订单数")
    private Integer totalOrderNum;

    @Schema(description = "总收益")
    private BigDecimal totalMoney;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
