package com.yanzu.module.member.service.iot.iotbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 14:47
 */
@Data
public class YunlabaOpVO<T> extends IotApiV2BaseReqVO{

    private String cmd_type="play";//命令字

    private T info;


}
