package com.yanzu.module.member.controller.app.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "miniapp - 房间信息 Response VO")
@Data
@ToString(callSuper = true)
public class AppRoomInfoRespVO {

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

    @Schema(description = "房间标签 逗号分隔")
    private String label;

    @Schema(description = "房间照片 逗号分隔")
    private String imageUrls;

    @Schema(description = "排序位置", example = "5068")
    private Integer sortId;

    @Schema(description = "禁用开始时间")
    private Date banTimeStart;

    @Schema(description = "禁用结束时间")
    private Date banTimeEnd;

    @Schema(description = "总完成订单数")
    private Integer totalOrderNum;

    @Schema(description = "总收益")
    private BigDecimal totalMoney;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;

}
