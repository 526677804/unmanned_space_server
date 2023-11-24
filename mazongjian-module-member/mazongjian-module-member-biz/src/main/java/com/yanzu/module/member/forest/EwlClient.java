package com.yanzu.module.member.forest;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.*;
import com.yanzu.module.member.service.iot.ewlbean.*;

public interface EwlClient {

    static final String baseUrl = "https://cn-apia.coolkit.cn";


    @Post(url = baseUrl + "/v2/user/oauth/token", headers = {
            "X-CK-Appid:${appid}",
            "Authorization:Sign ${authorization}",
            "Content-Type:application/json; charset=utf-8",
            "Host:cn-apia.coolkit.cn"
    })
    EwlBaseRespVO<EwlTokenRespVO> getToken(@Body EwlTokenReqVO reqVO, @Var("appid") String appid, @Var("authorization") String authorization);


    //Access Token 缺省 30 天失效（安全原因），此时可以不用重新登录获取 Access Token，而是使用 Refresh Token 刷新获取 Access Token
    @Post(url = baseUrl + "/v2/user/refresh", headers = {
            "X-CK-Appid:${appid}",
            "Authorization:Sign ${authorization}",
            "Content-Type:application/json; charset=utf-8",
            "Host:cn-apia.coolkit.cn"
    })
    JSONObject refresh(@Body EwlRefreshTokenReqVO reqVO, @Var("appid") String appid, @Var("authorization") String authorization);

//    @Post(url = baseUrl + "/v2/user/login", headers = {
//            "X-CK-Appid:${appid}",
//            "Authorization:Sign ${authorization}",
//            "Content-Type:application/json; charset=utf-8",
//            "Host:cn-apia.coolkit.cn"
//    })
//    EwlBaseRespVO<EwlUserLoginRespVO> login(@Body EwlUserLoginReqVO reqVO, @Var("appid") String appid, @Var("authorization") String authorization);
//

    @Get(url = baseUrl + "/v2/device/thing", headers = {
            "X-CK-Appid:${appid}",
            "Authorization:Bearer ${token}",
            "Host:cn-apia.coolkit.cn"
    })
    EwlBaseRespVO<JSONObject> getThing(@Query EwlGetThingReqVO reqVO, @Var("appid") String appid, @Var("token") String token);


    @Get(url = "https://cn-dispa.coolkit.cn/dispatch/app")
    JSONObject getSocketUrl();

}
