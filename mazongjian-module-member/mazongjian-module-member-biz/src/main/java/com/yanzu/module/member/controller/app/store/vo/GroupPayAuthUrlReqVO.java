package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupPayAuthUrlReqVO {

    @Schema(description = "门店id")
    @NotNull(message = "门店不能为空")
    private Long storeId;


    @Schema(description = "团购平台类型 1美团 2抖音 3快手")
    @NotNull(message = "团购平台类型不能为空")
    private Integer groupPayType;


}
