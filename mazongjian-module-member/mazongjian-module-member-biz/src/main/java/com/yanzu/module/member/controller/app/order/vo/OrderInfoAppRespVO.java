package com.yanzu.module.member.controller.app.order.vo;

import com.yanzu.module.member.controller.admin.orderinfo.vo.OrderInfoBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "miniapp - 订单管理 Response VO")
@Data
@ToString(callSuper = true)
public class OrderInfoAppRespVO  {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "2248")
    private Long orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

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
    private Date startTime;

    @Schema(description = "订单结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date endTime;

    @Schema(description = "订单价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "1698")
    private BigDecimal price;

    @Schema(description = "工作日折扣", example = "2948")
    private Integer workDiscount;

    @Schema(description = "实际支付价格", example = "6888")
    private BigDecimal payPrice;

    @Schema(description = "退款价格", example = "8062")
    private BigDecimal refundPrice;

    @Schema(description = "支付方式 值见字典", example = "1")
    private Integer payType;

    @Schema(description = "WIFI信息")
    private String wifiInfo;

    @Schema(description = "客服电话")
    private String kefuPhone;

    @Schema(description = "使用优惠券名称", example = "31071")
    private String couponName;

    @Schema(description = "状态 值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "创建时间/下单时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;

}
