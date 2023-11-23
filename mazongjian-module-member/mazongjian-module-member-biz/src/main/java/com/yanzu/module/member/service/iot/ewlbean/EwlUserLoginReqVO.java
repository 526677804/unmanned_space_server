package com.yanzu.module.member.service.iot.ewlbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.ewlbean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/23 14:14
 */
@Data
public class EwlUserLoginReqVO {

    private String countryCode = "+86";//电话区号区号，必须以"+"开头，比如"+86"
    private String phoneNumber = "17608045045";
    private String password = "qq127981";

}
