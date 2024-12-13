package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppGroupNoInfoRespVO {

    @Schema(description = "团购券名称")
    private String title;

    @Schema(description = "包含的小时")
    private Integer hours;
}
