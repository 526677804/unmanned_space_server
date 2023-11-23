package com.yanzu.module.member.service.iot.ewlbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.ewlbean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/23 13:32
 */
@Data
public class EwlTokenRespVO {
    private String accessToken;//授权凭证
    private Long atExpiredTime;//授权凭证的过期时间戳（毫秒）
    private String refreshToken;//刷新授权凭证的凭证
    private Long rtExpiredTime;//刷新授权凭证的凭证的过期时间戳（毫秒）
}
