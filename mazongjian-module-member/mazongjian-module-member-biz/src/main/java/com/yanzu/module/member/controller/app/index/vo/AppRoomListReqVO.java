package com.yanzu.module.member.controller.app.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppRoomListReqVO {

    @Schema(description = "门店id")
//    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "房间类别")
    private Integer roomClass;

    @Schema(description = "房间状态")
    private Integer status;


}
