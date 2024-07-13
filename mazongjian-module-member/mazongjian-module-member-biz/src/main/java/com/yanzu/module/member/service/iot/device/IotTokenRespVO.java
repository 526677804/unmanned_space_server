package com.yanzu.module.member.service.iot.device;


import lombok.Data;

@Data
public class IotTokenRespVO  {
    //访问令牌
    private String access_token;

    //刷新令牌
    private String refresh_token;

    //令牌类型
    private String token_type;

    //过期时间,单位：秒
    private Long expires_in;

    //授权范围
    private String scope;

}
