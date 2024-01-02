package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "miniapp - 房间详情 Response VO")
@Data
@ToString(callSuper = true)
public class AppRoomDetailRespVO {
    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16599")
    private Long roomId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String roomName;

    @Schema(description = "房间类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    private BigDecimal price;

    @Schema(description = "通宵场价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal tongxiaoPrice;

    @Schema(description = "房间标签 逗号分隔")
    private String label;

    @Schema(description = "房间照片 逗号分隔")
    private String imageUrls;

    @Schema(description = "排序位置", example = "0")
    private Integer sortId;

    @Schema(description = "禁用开始时间  HH:mm:ss")
    private String banTimeStart;

    @Schema(description = "禁用结束时间  HH:mm:ss")
    private String banTimeEnd;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;
}
