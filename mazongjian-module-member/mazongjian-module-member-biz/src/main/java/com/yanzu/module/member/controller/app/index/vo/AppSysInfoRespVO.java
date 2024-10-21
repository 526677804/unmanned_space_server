package com.yanzu.module.member.controller.app.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppSysInfoRespVO {


    @Schema(description = "版本")
    private String version;


}
