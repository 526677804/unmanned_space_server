package com.yanzu.module.member.service.iot.iotbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 15:53
 */
@Data
public class YunlabaInfoVO {

    private String tts;//要播放的文本

    private Integer inner = 10;//头部提示音，取值范围1-15

}
