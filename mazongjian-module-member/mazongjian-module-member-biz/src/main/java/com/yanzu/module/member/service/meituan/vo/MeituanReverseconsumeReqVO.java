package com.yanzu.module.member.service.meituan.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.meituan
 * @DESCRIPTION: 撤销验券参数vo
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/9/1 13:51
 */
@Data
public class MeituanReverseconsumeReqVO extends MeituanCommonParam {

    //套餐id，注意：对应deal_id，非dealgroup_id
    private String app_deal_id;

    //团购券码，必须未验证
    private String receipt_code;

    //美团点评店铺id，必须是团购的适用门店
    private String open_shop_uuid;

    //商家在自研系统或第三方服务商系统内登录的帐号，仅用于记录验券者的信息，该字段不参与任何验券校验逻辑
    private String app_shop_account;

    //商家在自研系统或第三方服务商系统内登陆的用户名，仅用于记录验券者的信息，该字段不参与任何验券校验逻辑
    private String app_shop_accountname;
}
