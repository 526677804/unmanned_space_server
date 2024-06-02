package com.yanzu.module.member.controller.app.pkg.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppMyPkgPageReqVO extends PageParam {


    @Schema(description = "门店id")
    private Long storeId;

    @Schema(description = "状态  0待使用 1已使用 2过期")
    private Integer status;

}
