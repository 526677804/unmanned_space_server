package com.yanzu.module.member.service.iot.ewlbean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.ewlbean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/24 14:22
 */
@Data
@AllArgsConstructor
public class EwlRefreshTokenReqVO {
    private String rt;//Refresh Token
}
