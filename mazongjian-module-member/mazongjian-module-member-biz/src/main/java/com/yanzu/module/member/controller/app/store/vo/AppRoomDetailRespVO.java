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

    @Schema(description = "房间类别", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer roomClass;

    @Schema(description = "房间简称", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String roomCallName;

    @Schema(description = "房间类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    private BigDecimal price;

    @Schema(description = "押金", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    private BigDecimal deposit;

    @Schema(description = "工作日单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    private BigDecimal workPrice;

    @Schema(description = "通宵场价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal tongxiaoPrice;

    @Schema(description = "房间标签 逗号分隔")
    private String label;

    @Schema(description = "房间照片 逗号分隔")
    private String imageUrls;

    @Schema(description = "排序位置", example = "0")
    private Integer sortId;

    @Schema(description = "云喇叭音量")
    private Integer yunlabaSound;

    @Schema(description = "禁用开始时间  HH:mm:ss")
    private String banTimeStart;

    @Schema(description = "禁用结束时间  HH:mm:ss")
    private String banTimeEnd;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;

    @Schema(description = "最大提前开始时间")
    private Integer leadHour;

    @Schema(description = "最大提前下单天数")
    private Integer leadDay;

    @Schema(description = "最小下单时间")
    private Integer minHour;

    @Schema(description = "跳过清洁")
    private Boolean jumpClear;

}
