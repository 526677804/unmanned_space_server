package com.yanzu.module.member.controller.app.reserve.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UpdateStockReqVO {

    @Schema(name = "门店id")
    private Long storeId;

    @Schema(name = "门店名称")
    private String storeName;

    @Schema(name = "房间ID")
    private Long roomId;

    @Schema(name = "房间名称")
    private String roomName;

    @Schema(name = "时段库存信息")
    private List<JSONObject> timePeriods;
}
