package com.yanzu.module.pay.controller.admin.demo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 订单创建 Request VO")
@Data
public class PayOrderCreateReqVO {


    @Schema(description = "用户openId", requiredMode = Schema.RequiredMode.REQUIRED, example = "zxfq12fq1312ewqwe")
    @NotNull(message = "用户openId不能为空")
    private String openId;

    @Schema(description = "商家订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023080112121217682000")
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "订单描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "17682")
    @NotNull(message = "订单描述不能为空")
    private String orderDesc;

    @Schema(description = "价格 单位/分", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "价格不能为空")
    private Integer price;


}
