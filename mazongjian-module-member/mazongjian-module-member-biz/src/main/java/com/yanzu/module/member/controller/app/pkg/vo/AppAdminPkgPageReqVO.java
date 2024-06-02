package com.yanzu.module.member.controller.app.pkg.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppAdminPkgPageReqVO extends PageParam {


    @Schema(description = "门店id")
    private Long storeId;


    @Schema(description = "是否启用")
    private Boolean enable;



}
