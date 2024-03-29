package com.yanzu.module.member.service.iot.iotBean;

import lombok.Data;

@Data
public class IotTokenRefushReqVO {
    //应用id
    private String client_id;

    //应用密钥
    private String secret;

    //token
    private String token;

}
