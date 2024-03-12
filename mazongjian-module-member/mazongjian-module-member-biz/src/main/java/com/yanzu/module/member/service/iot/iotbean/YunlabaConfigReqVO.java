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
    private Integer speed = 3;
    private Integer tone = 5;
    private String launch_tts = "欢迎您光临,本店无人值守,需要帮助请联系客服，请您文明娱乐,禁止从事赌博等违法行为.祝您玩的开心！";

}
