package com.yanzu.module.member.controller.app.productorder.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class AppSaveOrderReqVo {

    @NotNull(message = "非法操作，商品信息不能为空")
    private List<ProductInfoVo> productInfo;

    @NotNull(message = "房间不能为空")
    private Long roomId;


    private String mark;

}
