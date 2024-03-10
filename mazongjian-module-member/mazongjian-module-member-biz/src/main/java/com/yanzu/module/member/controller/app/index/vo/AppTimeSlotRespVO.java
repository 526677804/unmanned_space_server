package com.yanzu.module.member.controller.app.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppTimeSlotRespVO {

    @Schema(description = "小时数")
    private String hour;

    @Schema(description = "是否禁用")
    private Boolean disable;

}
