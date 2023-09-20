package com.yanzu.module.member.forest;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import org.hibernate.validator.constraints.URL;

public interface WorkWxClient {
    /**
     * 发送webhook消息
     */

    @Post(url = "{0}",contentType = "application/json")
    JSONObject sendMDMsg(String url, @JSONBody JSONObject reqVO);

}
