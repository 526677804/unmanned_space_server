package com.yanzu.module.member.service.iot.ewlbean;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.ewlbean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/23 14:52
 */
@Data
public class EwlGetThingReqVO {

    private String lang;//cn 返回中文信息，en 返回英文信息，默认 en

    private String familyid;//家庭 id，不填则默认为当前家庭

    private Integer num;//获取的数量，默认为 30，0 表示获取所有

    private Integer beginIndex;//从哪个序号开始获取列表数据，不填则默认为-9999999

}
