package com.yanzu.module.member.service.iot.bean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 16:09
 */
@Data
public class IotApiV2DataVO {

    private String device_sn;

    private Integer msg_id;

    private Integer type;

    private String cmd;

    private String cmd_type;

    private IotApiV2InfoVO info;


}
