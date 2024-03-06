package com.yanzu.module.member.service.iot.iotbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 14:45
 */
@Data
public class IotApiV2BaseReqVO<T> {

    private String app_id;

    private String app_secret;

    private String device_sn;//设备序列号

    private Integer type = 1;//固定为1

    private T data;

}
