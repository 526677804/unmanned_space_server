package com.yanzu.module.member.service.iot.groupPay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
public class IotGroupPaySelectByPhoneReqVO {

    @Schema(name = "手机号")
    private String mobile;

    @Schema(name = "店铺id")
    @NotNull(message = "店铺id不能为空")
    private Long storeId;

}
