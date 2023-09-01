package com.yanzu.module.member.service.meituan.vo;

import lombok.Data;

import java.util.UUID;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.meituan
 * @DESCRIPTION: 验券参数vo
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/9/1 13:51
 */
@Data
public class MeituanConsumeReqVO extends MeituanCommonParam {


    //请求id，用于标识幂等性
    private String requestid= UUID.randomUUID().toString();

    //团购券码，必须未验证
    private String receipt_code;

    //验券数量, 不可多于100个
    private Integer count = 1;

    //美团点评店铺id，必须是团购的适用门店
    private String open_shop_uuid;

    //商家在自研系统或第三方服务商系统内登录的帐号，仅用于记录验券者的信息，该字段不参与任何验券校验逻辑
    private String app_shop_account;

    //商家在自研系统或第三方服务商系统内登陆的用户名，仅用于记录验券者的信息，该字段不参与任何验券校验逻辑
    private String app_shop_accountname;


}
