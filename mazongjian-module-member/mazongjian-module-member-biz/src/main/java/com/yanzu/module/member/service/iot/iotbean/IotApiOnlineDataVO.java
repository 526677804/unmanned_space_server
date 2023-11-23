package com.yanzu.module.member.service.iot.iotbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 16:28
 */
@Data
public class IotApiOnlineDataVO extends IotApiBaseRespVO {
    private Integer online;//1在线0离线
    private Integer lockstatus;//1表示门打开状态，0表示门关闭状态，null为设备不支持该功能
}
