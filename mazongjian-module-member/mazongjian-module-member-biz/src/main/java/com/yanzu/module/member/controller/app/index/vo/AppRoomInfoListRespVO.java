package com.yanzu.module.member.controller.app.index.vo;

import com.yanzu.module.member.controller.app.order.vo.TimeSlotVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.index.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 17:08
 */

@Schema(description = "miniapp - 房间信息列表VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppRoomInfoListRespVO {

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16599")
    private Long roomId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String roomName;

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14069")
    private Long storeId;

    @Schema(description = "房间类型  值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "20571")
    private BigDecimal price;

    @Schema(description = "房间标签 逗号分隔")
    private String label;

    @Schema(description = "房间照片 逗号分隔")
    private String imageUrls;

    @Schema(description = "排序位置", example = "5068")
    private Integer sortId;

    @Schema(description = "订单/预约开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startTime;

    @Schema(description = "订单/预约结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date endTime;

    @Schema(description = "状态 值见字典", example = "2")
    private Integer status;

    @Schema(description = "不可用的时间段,为空则表示未来5天都可以使用")
    private List<TimeSlotVO> disabledTimeSlot;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;
}
