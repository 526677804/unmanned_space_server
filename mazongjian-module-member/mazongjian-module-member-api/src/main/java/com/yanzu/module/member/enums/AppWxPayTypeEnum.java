package com.yanzu.module.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum AppWxPayTypeEnum implements Serializable {

    ORDER(1),//下单
    RENEW(2),//续费
    RECHARGE(3),//充值
    PKG(4),//购买套餐

    ;


    private final Integer value;


}
