package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2024/6/19 10:58
 */
@Data
public class PreGroupNoReqVO {


    @Schema(description = "门店id")
    @NotNull(message = "门店id不能为空")
    private Long storeId;


    @Schema(description = "团购券码")
    @NotNull(message = "团购券码不能为空")
    private String code;

}
