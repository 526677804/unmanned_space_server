package com.yanzu.module.member.controller.admin.payorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.checkerframework.checker.units.qual.N;

import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 支付订单更新 Request VO")
@Data
@ToString(callSuper = true)
public class PayOrderUpdateReqVO {


    @Schema(description = "id")
    @NotNull(message = "数据不能为空")
    private Long id;

    @Schema(description = "支付金额")
    private Integer price;

}
