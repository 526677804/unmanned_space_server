package com.yanzu.module.member.service.iot.iotbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 14:45
 */
@Data
public class IotApiBaseReqVO {

    private String appid;

    private String appsecret;

    private String sn;//设备序列号
}
