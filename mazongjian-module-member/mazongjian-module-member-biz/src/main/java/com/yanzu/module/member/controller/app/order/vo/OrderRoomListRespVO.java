package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 15:33
 */
@Schema(description = "miniapp - 更换房间 房间列表 Response VO")
@Data
@ToString(callSuper = true)
public class OrderRoomListRespVO {

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16599")
    private Long roomId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String roomName;

    @Schema(description = "房间类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "房间状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "14069")
    private Integer status;

    @Schema(description = "不可用的时间段,为空则表示未来5天都可以使用")
    private List<TimeSlotVO> disabledTimeSlot;

    @Schema(description = "是否可选 true为可选", example = "true")
    private Boolean available;

}
