package com.yanzu.module.member.service.iot.ttlockBean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.ttlockBean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2024/3/22 18:28
 */
@Data
public class TTLockKeyReqVO {

    private String clientId;

    private String accessToken;

    private Integer lockId;

    private Long date;

}
