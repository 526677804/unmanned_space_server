package com.yanzu.module.member.service.iot.ewlbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.iotbean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/23 13:30
 */
@Data
public class EwlBaseRespVO<T> {

    private Integer error;

    private String msg;

    private T data;

}
