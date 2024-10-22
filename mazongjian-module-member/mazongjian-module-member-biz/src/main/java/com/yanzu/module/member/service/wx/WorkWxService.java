package com.yanzu.module.member.service.wx;

import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import com.yanzu.module.member.dal.dataobject.pkginfo.PkgInfoDO;

import java.math.BigDecimal;
import java.util.Date;

public interface WorkWxService {
    void sendOrderMsg(Long storeId, Long userId, String roomName, BigDecimal price, CouponInfoDO couponInfoDO, PkgInfoDO pkgInfoDO, Integer payType, Integer groupPayType, String orderNo, Date startTime, Date endTime);

    void sendOrderCancelMsg(Long storeId, Long userId, Long roomId, BigDecimal price, CouponInfoDO couponInfoDO, Integer payType, Integer groupPayType, String orderNo, boolean isadmin);


    void sendCloseOrderMsg(Long storeId, Long userId, Long roomId, Integer payType, Integer groupPayType, String orderNo);

    void sendGameMsg(Long storeId, String content);

    void sendClearMsg(String webhookUrl, String content);

    void sendRenewMsg(Long storeId, Long userId, String roomName, BigDecimal price, Integer payType, String orderNo, Date endTime, CouponInfoDO couponInfoDO,boolean isAdmin);

    void sendRechargeMsg(Long storeId, Long userId, BigDecimal price, BigDecimal giftPrice);

    void sendAdminRechargeMsg(Long storeId, Long userId, Long adminUserId, BigDecimal giftPrice);

    void sendGiftCouponMsg(Long storeId, Long userId, String couponName, Integer roomType);

    void sendMeiTuanScopeMsg(Long storeId);

    void sendChangeRoomMsg(Long storeId, String orderNo, Date startTime, Date endTime, String oldRoomName, String newRoomName, Long userId);

    void sendOrderChangeUserMsg(Long storeId, String orderNo, Long roomId, Date startTime, Date endTime, Long userId);

    void sendUseGroupNoMsg(GroupPayInfoDO groupPayInfoDO, Long userId);

    void sendClearRoomMsg(Long storeId, Long roomId, Long loginUserId, String type);

    void sendChangeMsg(Long storeId, String orderNo, Date startTime, Date endTime, Long oldRoom, Long newRoom, Long userId);

    void sendClearFinishMsg(Long storeId, Long roomId, Long loginUserId, String type);

    void sendRepeatOrderMsg(Long storeId, String roomName, Date startTime, Date endTime, Long userId);

    void sendOrderClearMsg(Long storeId, String roomName, Date startTime, Date endTime);

    void sendOrderSubmitMsg(Long storeId, Long userId, String mobile, String roomName, String orderNo, Date startTime, Date endTime);

    void sendCallMsg(Long storeId, String tts);

}
