package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "miniapp - 房间详情 Req VO")
@Data
@ToString(callSuper = true)
public class AppRoomDetailReqVO {
    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16599")
    private Long roomId;
    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16599")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotNull(message = "房间名称不能为空")
    private String roomName;

    @Schema(description = "房间类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "房间类型不能为空")
    private Integer type;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "20571")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @Schema(description = "通宵场价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal tongxiaoPrice;

    @Schema(description = "房间标签 逗号分隔")
    @NotNull(message = "房间标签不能为空")
    private String label;

    @Schema(description = "房间照片 逗号分隔")
    @NotNull(message = "房间照片不能为空")
    private String imageUrls;

    @Schema(description = "排序位置", example = "0")
    private Integer sortId;

    @Schema(description = "禁用开始时间 HH:mm:ss")
    private String banTimeStart;

    @Schema(description = "禁用结束时间 HH:mm:ss")
    private String banTimeEnd;

}
