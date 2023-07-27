package com.yanzu.module.member.service.iot.bean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 15:03
 */
@Data
public class IotApiBaseRespVO {

    private Integer state;

    private Integer state_code;

    private String state_msg;


}
