package com.yanzu.module.member.service.iot.bean;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 14:45
 */
@Data
public class IotApiV2BaseReqVO<T> {

    private String app_id = "d5bac4c4fbc731b59b59914fd4365efe";

    private String app_secret = "76d0a8c2b9003923c0e782b216a74bbe";

    private String device_sn;//设备序列号

    private Integer type = 1;//固定为1

    private T data;

}
