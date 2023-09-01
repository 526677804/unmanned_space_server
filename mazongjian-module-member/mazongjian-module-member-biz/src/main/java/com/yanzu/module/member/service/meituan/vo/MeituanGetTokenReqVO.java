package com.yanzu.module.member.service.meituan.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.meituan
 * @DESCRIPTION: 获取token参数vo
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/9/1 13:51
 */
@Data
public class MeituanGetTokenReqVO  {
    //点评到综开放平台分配给应用的AppKey。
    private String app_key;

    //点评到综开放平台分配给应用的app_secret。
    private String app_secret;

    //获取session的授权码
    private String auth_code;

    //填 authorization_code
    private String grant_type="authorization_code";

    //获取授权码的回调地址
    private String redirect_url;

}
