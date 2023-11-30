package com.yanzu.module.member.service.iot.iotbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.iotbean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/28 16:40
 */
@Data
public class YunlabaConfigReqVO {
// "volume":5,  // 0-9，音量由小到大，默认为中间值
//            "speed":5,   // 0-9，语速由慢到快，默认为中间值正常语速
//            "tone":5,     // 0-9，语调由低到高，默认为中间值正常语调
//            "launch_tts":" " // 网络连接成功后播报

    private Integer volume = 2;
    private Integer speed = 4;
    private Integer tone = 4;
    private String launch_tts = " ";

}
