package com.yanzu.module.member.controller.app.productorder.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Data
@NotNull
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AppUserOrderPageRespVO {

    @Schema(name = "商品订单id")
    private Long orderId;

    @Schema(name = "商品订单编号")
    private String orderNo;

    @Schema(name = "店铺id")
    private Long storeId;

    @Schema(name = "房间名称")
    private String roomName;

    @Schema(name = "用户姓名")
    private String userName;

    @Schema(name = "用户手机号")
    private String userPhone;

    @Schema(name = "商品信息")
    private List<ProductInfoVo> productInfoVoList;

    @Schema(name = "订单总价")
    private Integer totalPrice;

    @Schema(name = "实际支付金额")
    private Integer payPrice;

    @Schema(name = "支付时间")
    private LocalDateTime payTime;

    @Schema(name = "订单状态")
    private Long status;

    @Schema(name = "备注")
    private String mark;

    @Schema(name = "支付类型")
    private Long payType;

    @Schema(name = "商品订单创建时间")
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date createTime;


}
