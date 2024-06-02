package com.yanzu.module.member.controller.app.pkg.vo;

import cn.hutool.extra.spring.SpringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppBuyPkgReqVO {


    @NotNull(message = "套餐不能为空")
    @Schema(description = "套餐id 必传")
    private Long pkgId;


    @Schema(description = "预下单返回的订单编号 必传", requiredMode = Schema.RequiredMode.REQUIRED, example = "20231123123123123123")
    @NotNull(message = "orderNo不能为空")
    private String orderNo;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "支付金额,单位为分 必传", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "支付金额不能为空")
    private Integer price;


}
