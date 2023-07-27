package com.yanzu.module.member.service.iot.bean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 16:41
 */
@Data
public class IotCallbakReqVO {

    private String cmd;//OnLine 上线  OffLine 离线  lock_close 关  lock_open 开

    private String device_sn;//设备


}
