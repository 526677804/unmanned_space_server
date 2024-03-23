package com.yanzu.module.member.forest;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.*;
import com.dtflys.forest.http.ForestResponse;
import com.yanzu.module.member.service.douyin.vo.DouyinCancelReqVO;
import com.yanzu.module.member.service.douyin.vo.DouyinGetClientTokenReqVO;
import com.yanzu.module.member.service.douyin.vo.DouyinGetCodeReqParam;
import com.yanzu.module.member.service.douyin.vo.DouyinVerifyReqVO;

// 打开整个接口的自动重定向
@Redirection
public interface DouyinClient {

    //生成token
    @Post(url = "https://open.douyin.com/oauth/client_token/")
    JSONObject getClientToken(@JSONBody DouyinGetClientTokenReqVO reqVO);


    @Get(url = "https://open.douyin.com/platform/oauth/connect/", contentType = "application/json")
    JSONObject getCode(@Body DouyinGetCodeReqParam reqVO);

    //验券准备
    @Get(url = "{0}", contentType = "application/json", headers = {
            "access-token:${accessToken}"
    })
    JSONObject prepare(String url, @Var("accessToken") String accessToken);

    @Get(url = "{0}")
    @Redirection(false)
    //关闭重定向
    ForestResponse<String> queryCode(String url);

    //验券
    @Post(url = "https://open.douyin.com/goodlife/v1/fulfilment/certificate/verify/"
            , contentType = "application/json", headers = {
            "access-token:${accessToken}"
    })
    JSONObject verify(@JSONBody DouyinVerifyReqVO reqVO, @Var("accessToken") String accessToken);

    //撤销验券
    @Post(url = "https://open.douyin.com/goodlife/v1/fulfilment/certificate/cancel/"
            , contentType = "application/json", headers = {
            "access-token:${accessToken}"
    })
    JSONObject cancel(@JSONBody DouyinCancelReqVO reqVO, @Var("accessToken") String accessToken);


}
