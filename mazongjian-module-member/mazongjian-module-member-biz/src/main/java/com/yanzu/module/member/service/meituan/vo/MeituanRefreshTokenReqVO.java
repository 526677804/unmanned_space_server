package com.yanzu.module.member.service.meituan.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.meituan
 * @DESCRIPTION: 获取token参数vo
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/9/1 13:51
 */
@Data
public class MeituanRefreshTokenReqVO {
    //点评到综开放平台分配给应用的AppKey。
    private String app_key;

    //点评到综开放平台分配给应用的app_secret。
    private String app_secret;

    //填 refresh_token
    private String grant_type = "refresh_token";

    //用来刷新授权的的refresh_token值
    private String refresh_token;

}
