package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppStoreSoundInfoReqVO {



    @Schema(description = "门店ID")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "欢迎语")
    private String welcomeText;

    @Schema(description = "结束30分钟提示语")
    private String endText30;
    @Schema(description = "结束5分钟提示语")
    private String endText5;
    @Schema(description = "深夜消费提示语")
    private String nightText;


}
