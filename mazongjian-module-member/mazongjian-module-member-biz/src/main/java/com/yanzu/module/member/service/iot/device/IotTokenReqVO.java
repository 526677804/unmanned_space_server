package com.yanzu.module.member.service.iot.device;

import lombok.Data;

@Data
public class IotTokenReqVO {
    //应用id
    private String client_id;

    //应用密钥
    private String secret;

    //授权获得的code
    private String code;

}
