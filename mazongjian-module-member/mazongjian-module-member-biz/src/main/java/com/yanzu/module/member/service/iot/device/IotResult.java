package com.yanzu.module.member.service.iot.device;

import lombok.Data;

@Data
public class IotResult<T> {

    private Integer code;

    private String msg;

    private T data;


}
