package com.yanzu.module.member.service.workwx;

import java.math.BigDecimal;
import java.util.Date;

public interface WorkWxService {
    void sendOrderMsg(Long storeId, Long userId,String roomName,BigDecimal price,Integer payType,String orderNo,Date startTime,Date endTime);

    void sendOrderCancelMsg(Long storeId, Long userId,Long roomId,BigDecimal price,Integer payType,String orderNoe);

    void sendGameMsg(Long storeId, String content);

    void sendClearMsg(String webhookUrl, String content);

    void sendRenewMsg(Long storeId, Long userId, String roomName, BigDecimal price, Integer payType, String orderNo, Date endTime, boolean isAdmin);

    void sendRechargeMsg(Long storeId, Long userId, BigDecimal price, BigDecimal giftPrice);


}
