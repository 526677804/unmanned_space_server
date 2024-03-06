package com.yanzu.module.member.service.iot.iotbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 15:42
 */
@Data
public class KongkaiOpVO {

    //业务参数：操作命令
    //turnon：开断路器
    //turnoff：关断路器
    //restart：重启断路器
    //wifi_config：wifi版进入配网模式
    private String cmd_type;

    //业务参数：命令信息
    private Object info;

}
