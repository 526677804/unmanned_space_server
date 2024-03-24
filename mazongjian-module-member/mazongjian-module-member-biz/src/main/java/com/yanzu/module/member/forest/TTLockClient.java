package com.yanzu.module.member.forest;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Query;
import com.yanzu.module.member.service.iot.ttlockBean.TTLockKeyReqVO;
import com.yanzu.module.member.service.iot.ttlockBean.TTLockTokenReqVO;

public interface TTLockClient {
    /**
     * 获取单把钥匙
     */

    @Post(url = "https://cnapi.ttlock.com/oauth2/token")
    JSONObject getToken(@Query TTLockTokenReqVO reqVO);


    @Post(url = "https://cnapi.ttlock.com/v3/key/get")
    JSONObject getKey(@Query TTLockKeyReqVO reqVO);

}
