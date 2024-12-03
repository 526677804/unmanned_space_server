package com.yanzu.module.member.controller.app.productorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppHaveOrderStoreRespVo {

    @Schema(name = "店铺Id")
    private Long storeId;


    @Schema(name = "店铺名称")
    private String storeName;
}
