package com.yanzu.module.member.service.iot.device;

import lombok.Data;

@Data
public class IotTokenRefushReqVO {
    //应用id
    private String client_id;

    //应用密钥
    private String client_secret;

    //token
    private String refresh_token;

}
