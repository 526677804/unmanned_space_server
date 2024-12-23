package com.yanzu.module.member.controller.admin.groupPay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 团购支付信息更新 Request VO")
@Data
@ToString(callSuper = true)
public class GroupPayInfoUpdateReqVO  {

    @Schema(description = "id")
    @NotNull(message = "数据不能为空")
    private Long id;

    @Schema(description = "支付金额")
    private Integer groupPayPrice;

}
