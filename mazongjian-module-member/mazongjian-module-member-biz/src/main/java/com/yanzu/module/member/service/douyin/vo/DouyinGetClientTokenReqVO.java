package com.yanzu.module.member.service.douyin.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.douyin.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/8 12:30
 */
@Data
public class DouyinGetClientTokenReqVO {

    //应用唯一标识
    private String client_key;

    //应用唯一标识对应的密钥
    private String client_secret;

    //固定值“client_credential”
    private String grant_type="client_credential";
}
