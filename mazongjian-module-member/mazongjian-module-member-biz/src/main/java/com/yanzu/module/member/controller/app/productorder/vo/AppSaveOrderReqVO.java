package com.yanzu.module.member.controller.app.productorder.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString
public class AppSaveOrderReqVO {

    @NotNull(message = "非法操作，商品信息不能为空")
    private List<ProductInfoVO> productInfo;

    @NotNull(message = "房间不能为空")
    private Long roomId;


    private String mark;

}
