package com.yanzu.module.member.service.iot.device;

import lombok.Data;

@Data
public class IotAuthReqVO {


    //授权类型 固定值code
    private String response_type = "code";

    //应用id
    private String client_id;

    //应用密钥
    private String secret;

    //回调链接 用于接收code
    private String redirect_uri;

    //任意值 回调的时候会带回来
    private String state = "1";

    //授权范围  固定写1
    private String scope = "1";

}
