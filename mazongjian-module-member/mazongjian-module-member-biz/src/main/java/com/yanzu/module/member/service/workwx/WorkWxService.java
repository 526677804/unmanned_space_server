package com.yanzu.module.member.service.workwx;

public interface WorkWxService {
    void sendOrderMsg(Long storeId, String content);
    void sendGameMsg(Long storeId, String content);
}
