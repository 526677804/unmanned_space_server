package com.yanzu.module.member.service.workwx;

import com.alibaba.fastjson.JSONObject;

public interface WorkWxService {
    void sendMDMsg(Long storeId,String content);
}
