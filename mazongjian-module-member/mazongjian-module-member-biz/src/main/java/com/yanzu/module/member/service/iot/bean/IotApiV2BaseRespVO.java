package com.yanzu.module.member.service.iot.bean;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

/**
 * @PACKAGE_NAME: com.yanzu.iot.bean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 15:03
 */
@Data
public class IotApiV2BaseRespVO<T> {

    private Integer code;

    private String msg;

    private T data;


}
