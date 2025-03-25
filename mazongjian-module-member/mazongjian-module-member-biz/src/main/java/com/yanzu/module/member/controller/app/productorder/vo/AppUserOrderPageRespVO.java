package com.yanzu.module.member.controller.app.productorder.vo;

import com.alibaba.fastjson.annotation.JSONField;
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
public class AppUserOrderPageRespVO {

    @Schema(description = "商品订单id")
    private Long orderId;

    @Schema(description = "商品订单编号")
    private String orderNo;

    @Schema(description = "店铺id")
    private Long storeId;

    @Schema(description = "房间名称")
    private String roomName;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "用户手机号")
    private String userPhone;

    @Schema(description = "商品信息")
    private List<ProductInfoVo> productInfoVoList;

    private String productInfoJson;

    @Schema(description = "订单总价")
    private Integer totalPrice;

    @Schema(description = "实际支付金额")
    private Integer payPrice;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "订单状态")
    private Long status;

    @Schema(description = "备注")
    private String mark;

    @Schema(description = "支付类型")
    private Long payType;

    @Schema(description = "商品订单创建时间")
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    private Date createTime;


}
