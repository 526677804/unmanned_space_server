package com.yanzu.module.member.controller.app.store.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Data
public class AppRoomInfoListRespVO {

    @Schema(description = "门店id")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16599")
    private Long roomId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String roomName;

    @Schema(description = "房间简称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String roomCallName;

    @Schema(description = "房间类别", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer roomClass;

    @Schema(description = "密码锁data 用于离线蓝牙开锁")
    private String lockData;

    @Schema(description = "网关id 用于远程开锁")
    private Long gatewayId;

    @Schema(description = "房间类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "最近的订单/预约开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date startTime;

    @Schema(description = "最近的订单/预约结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date endTime;

    @Schema(description = "保洁任意开门")
    private Boolean clearOpenDoor;

    @Schema(description = "空调控制器")
    private Integer kongtiaoCount;
}
