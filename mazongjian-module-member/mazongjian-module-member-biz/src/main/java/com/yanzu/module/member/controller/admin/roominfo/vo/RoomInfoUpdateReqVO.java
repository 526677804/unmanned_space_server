package com.yanzu.module.member.controller.admin.roominfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 房间管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoomInfoUpdateReqVO extends RoomInfoBaseVO {

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25625")
    @NotNull(message = "房间id不能为空")
    private Long roomId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotNull(message = "房间名称不能为空")
    private String roomName;

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "27907")
    @NotNull(message = "门店id不能为空")
    private Long storeId;

    @Schema(description = "房间类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "房间类型不能为空")
    private Integer type;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "31263")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @Schema(description = "排序位置", example = "32351")
    private Integer sortId;

    @Schema(description = "总完成订单数")
    private Integer totalOrderNum;

    @Schema(description = "状态", example = "2")
    private Integer status;

}
