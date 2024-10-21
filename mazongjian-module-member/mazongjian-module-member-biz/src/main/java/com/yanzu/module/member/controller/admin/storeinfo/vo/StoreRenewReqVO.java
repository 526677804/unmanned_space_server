package com.yanzu.module.member.controller.admin.storeinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StoreRenewReqVO {


    @Schema(description = "门店ID")
    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @Schema(description = "状态")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
