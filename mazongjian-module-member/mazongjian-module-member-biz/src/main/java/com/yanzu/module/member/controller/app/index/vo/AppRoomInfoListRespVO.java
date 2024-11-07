package com.yanzu.module.member.controller.app.index.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

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

    @Schema(description = "房间类别  值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer roomClass;

    @Schema(description = "房间类型  值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer type;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    private BigDecimal price;

    @Schema(description = "押金", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    private BigDecimal deposit;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    private BigDecimal workPrice;

    @Schema(description = "通宵场价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal tongxiaoPrice;

    @Schema(description = "房间标签 逗号分隔")
    private String label;

    @Schema(description = "房间照片 逗号分隔")
    private String imageUrls;

    @Schema(description = "排序位置", example = "5068")
    private Integer sortId;

    @Schema(description = "每日禁用开始时间")
    private String banTimeStart;

    @Schema(description = "每日禁用结束时间")
    private String banTimeEnd;

    @Schema(description = "最近的订单/预约开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date startTime;

    @Schema(description = "最近的订单/预约结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date endTime;

    @Schema(description = "状态 值见字典", example = "2")
    private Integer status;

    @Schema(description = "不可用的时间段,从当前小时开始")
    private List<AppTimeSlotRespVO> timeSlot;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;

    @Schema(description = "通宵开始小时")
    private Integer txStartHour;
    @Schema(description = "通宵小时时长")
    private Integer txHour;
    @Schema(description = "启用工作日价格")
    private Boolean enableWorkPrice;
    @Schema(description = "最大提前开始时间")
    private Integer leadHour;
    @Schema(description = "最大提前下单天数")
    private Integer leadDay;
    @Schema(description = "最小下单时间")
    private Integer minHour;
    @Schema(description = "订单清洁时间")
    private Integer clearTime;


    @Schema(description = "已被预定订单时间列表")
    private List<AppOrderTimeVO> orderTimeList;


}
