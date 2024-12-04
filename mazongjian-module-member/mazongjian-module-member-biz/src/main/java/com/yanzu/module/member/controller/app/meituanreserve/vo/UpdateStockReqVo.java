package com.yanzu.module.member.controller.app.meituanreserve.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UpdateStockReqVo {

    @Schema(name = "门店id")
    private Long storeId;

    @Schema(name = "三方房间ID")
    private Long thirdPartyRoomId;

    @Schema(name = "房间名称")
    private String roomName;

    @Schema(name = "时段库存信息 注：类型为对象类型，详见扩展参数desk_sold_time_periods  ")
    private List<DeskSoldTimePeriodsSub> deskSoldTimePeriods;
}
