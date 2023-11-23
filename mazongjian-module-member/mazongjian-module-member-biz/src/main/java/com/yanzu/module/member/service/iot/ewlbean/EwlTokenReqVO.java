package com.yanzu.module.member.service.iot.ewlbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.ewlbean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/23 13:32
 */
@Data
public class EwlTokenReqVO {
    private String code;//授权码
    private String redirectUrl;//回调地址
    private String grantType = "authorization_code";//目前暂时固定为 authorization_code
}
