package com.yanzu.module.member.service.iot.ewlbean;

import lombok.Data;
import netscape.javascript.JSObject;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.ewlbean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/23 14:14
 */
@Data
public class EwlUserLoginRespVO {
    private JSObject user;
    private String at;//Access Token
    private String rt;//Refresh Token
    private String region;//用户所属区域 cn=中国区 as=亚洲区 us=美洲区 eu=欧洲区
}
