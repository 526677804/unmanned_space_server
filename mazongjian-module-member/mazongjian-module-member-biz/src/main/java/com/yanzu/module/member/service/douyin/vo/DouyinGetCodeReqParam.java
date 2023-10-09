package com.yanzu.module.member.service.douyin.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.douyin.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/8 14:05
 */
@Data
public class DouyinGetCodeReqParam {

    //应用唯一标识
    private String client_key;

     //写死为 code 即可
    private String response_type="code";

     //应用授权作用域，多个授权作用域以英文逗号（,）分隔
    private String scope;

     //授权成功后的回调地址
    private String redirect_uri;

}
