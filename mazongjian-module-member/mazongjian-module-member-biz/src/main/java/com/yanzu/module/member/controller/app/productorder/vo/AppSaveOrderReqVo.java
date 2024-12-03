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

    @NotNull(message = "非法操作，无店铺id")
    private Long storeId;

    @NotNull(message = "非法操作，无店铺名称")
    private String storeName;

    @NotNull(message = "非法操作，无商品总价")
    private BigDecimal totalPrice;

    @NotNull(message = "用户信息未完善，缺少用户名")
    private String userName;

    @NotNull(message = "用户信息未完善，缺少用户名联系方式")
    private String userPhone;

    private String mark;

}
