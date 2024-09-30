package com.yanzu.module.member.controller.app.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Schema(description = "miniapp - 订单管理 Response VO")
@Data
@ToString(callSuper = true)
public class OrderInfoAppRespVO {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2248")
    private Long orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "订单key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderKey;

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long storeId;

    @Schema(description = "房间id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long roomId;

    @Schema(description = "房间图片")
    private String roomImg;

    @Schema(description = "房间单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private BigDecimal roomPrice;

    @Schema(description = "闲时单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private BigDecimal workPrice;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "2319")
    private String roomName;

    @Schema(description = "房间类型  值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String roomType;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "2319")
    private String storeName;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4765")
    private Long userId;

    @Schema(description = "订单开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    private Date startTime;

    @Schema(description = "订单结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    private Date endTime;

    @Schema(description = "订单时长 单位/小时", requiredMode = Schema.RequiredMode.REQUIRED, example = "4.0")
    private Float orderHour;

    @Schema(description = "订单价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "1698")
    private BigDecimal price;

    @Schema(description = "押金", requiredMode = Schema.RequiredMode.REQUIRED, example = "1698")
    private BigDecimal deposit;

    @Schema(description = "密码锁data 用于离线蓝牙开锁")
    private String lockData;

    @Schema(description = "网关id 用于远程开锁")
    private Long gatewayId;

    @Schema(description = "是否存在空调控制器")
    private Integer kongtiaoCount;

    @Schema(description = "实际支付价格", example = "6888")
    private BigDecimal payPrice;

    @Schema(description = "退款价格", example = "8062")
    private BigDecimal refundPrice;

    @Schema(description = "支付方式 值见字典", example = "1")
    private Integer payType;

    @Schema(description = "WIFI信息")
    private String wifiInfo;

    @Schema(description = "WIFI密码")
    private String wifiPwd;

    @Schema(description = "客服电话")
    private String kefuPhone;

    @Schema(description = "使用优惠券名称", example = "31071")
    private String couponName;

    @Schema(description = "状态 值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "是否允许续费",  example = "false")
    private Boolean renewBtn;

    @Schema(description = "创建时间/下单时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date createTime;


}
