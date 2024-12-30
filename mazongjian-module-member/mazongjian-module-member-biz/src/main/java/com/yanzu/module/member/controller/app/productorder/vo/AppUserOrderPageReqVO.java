package com.yanzu.module.member.controller.app.productorder.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppUserOrderPageReqVO extends PageParam {

    @Schema(name = "门店id")
    private Long storeId;

    @Schema(name = "商品订单状态")
    private Long status;

}
