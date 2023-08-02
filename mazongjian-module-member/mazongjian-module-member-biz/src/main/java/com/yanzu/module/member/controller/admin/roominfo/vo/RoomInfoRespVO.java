package com.yanzu.module.member.controller.admin.roominfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 房间管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoomInfoRespVO extends RoomInfoBaseVO {

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25625")
    private Long roomId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String roomName;

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "27907")
    private Long storeId;

    @Schema(description = "房间类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "31263")
    private BigDecimal price;

    @Schema(description = "房间标签")
    private String label;

    @Schema(description = "排序位置", example = "32351")
    private Integer sortId;

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

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
