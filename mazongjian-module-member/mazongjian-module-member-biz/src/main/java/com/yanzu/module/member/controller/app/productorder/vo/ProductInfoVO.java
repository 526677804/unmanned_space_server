package com.yanzu.module.member.controller.app.productorder.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInfoVO {

    private Long cate_id;

    private Long id;

    private String image;

    private String name;

    private Long number;

    private BigDecimal price;

    private Long shopId;

    private String valueStr;

}
