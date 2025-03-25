package com.yanzu.module.member.controller.app.reserve.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UpdateStockReqVO {

     @Schema(description = "门店id")
    private Long storeId;

     @Schema(description = "门店名称")
    private String storeName;

     @Schema(description = "房间ID")
    private Long roomId;

     @Schema(description = "房间名称")
    private String roomName;

     @Schema(description = "时段库存信息")
    private List<JSONObject> timePeriods;
}
