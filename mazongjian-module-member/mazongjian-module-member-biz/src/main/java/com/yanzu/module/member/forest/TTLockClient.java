package com.yanzu.module.member.forest;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Query;
import com.yanzu.module.member.service.iot.ttlockBean.TTLockKeyReqVO;
import com.yanzu.module.member.service.iot.ttlockBean.TTLockTokenReqVO;

public interface TTLockClient {
    /**
     * 获取单把钥匙
     */

    @Get(url = "https://cnapi.ttlock.com/oauth2/token",contentType = "application/x-www-form-urlencoded")
    JSONObject getToken(@Query TTLockTokenReqVO reqVO);


    @Get(url = "https://cnapi.ttlock.com/v3/key/get",contentType = "application/x-www-form-urlencoded")
    JSONObject getKey(@Query TTLockKeyReqVO reqVO);

}
