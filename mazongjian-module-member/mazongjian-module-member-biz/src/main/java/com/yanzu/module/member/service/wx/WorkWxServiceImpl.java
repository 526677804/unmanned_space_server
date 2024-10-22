package com.yanzu.module.member.service.wx;

import com.alibaba.fastjson.JSONObject;
import com.yanzu.framework.common.util.date.DateUtils;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import com.yanzu.module.member.dal.dataobject.pkginfo.PkgInfoDO;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.dal.mysql.user.AppUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.forest.WorkWxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
@Validated
@Slf4j
public class WorkWxServiceImpl implements WorkWxService {

    @Autowired
    private WorkWxClient workWxClient;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;
    @Resource
    private AppUserMapper appUserMapper;

    @Override
    @Async
    public void sendOrderMsg(Long storeId, Long userId, String roomName, BigDecimal price, CouponInfoDO couponInfoDO, PkgInfoDO pkgInfoDO, Integer payType, Integer groupPayType, String orderNo, Date startTime, Date endTime) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        log.info("发送订单消息到配置的企业微信");
        StringBuffer sb = new StringBuffer();
        sb.append("用户下单通知\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">手机号码:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
//        sb.append(">订单金额:<font color=\"warning\">").append(price).append("</font>\n");
        sb.append(">支付方式:<font color=\"warning\">").append(getPayTypeStr(payType)).append("</font>\n");
        if (!ObjectUtils.isEmpty(pkgInfoDO)) {
            sb.append(">套餐名称:<font color=\"warning\">").append(pkgInfoDO.getPkgName()).append("</font>\n");
        }
        if (!ObjectUtils.isEmpty(couponInfoDO)) {
            sb.append(">使用卡券:<font color=\"warning\">").append(couponInfoDO.getCouponName()).append("</font>\n");
        }
        if (!ObjectUtils.isEmpty(groupPayType)) {
            sb.append(">团购平台:<font color=\"warning\">").append(getGroupPayTypeStr(groupPayType)).append("</font>\n");
        }
        sb.append(">开始时间:<font color=\"warning\">").append(DateUtils.dateToStr(startTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">结束时间:<font color=\"warning\">").append(DateUtils.dateToStr(endTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
//        重复发一遍手机号
//        JSONObject msg2 = new JSONObject();
//        msg2.put("msgtype", "text");
//        JSONObject text = new JSONObject();
//        text.put("content", memberUserDO.getMobile());
//        msg2.put("text", text);
//        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg2);
    }

    @Override
    @Async
    public void sendOrderCancelMsg(Long storeId, Long userId, Long roomId, BigDecimal price, CouponInfoDO couponInfoDO, Integer payType, Integer groupPayType, String orderNo, boolean isadmin) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        String roomName = roomInfoMapper.getNameById(roomId);
        log.info("发送订单消息到配置的企业微信");
        StringBuffer sb = new StringBuffer();
        if (isadmin) {
            sb.append("管理员取消订单通知\n");
        } else {
            sb.append("用户取消订单通知\n");
        }
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">手机号码:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
//        sb.append(">订单金额:<font color=\"warning\">").append(price).append("</font>\n");
        sb.append(">支付方式:<font color=\"warning\">").append(getPayTypeStr(payType)).append("</font>\n");
        if (!ObjectUtils.isEmpty(couponInfoDO)) {
            sb.append(">使用卡券:<font color=\"warning\">").append(couponInfoDO.getCouponName()).append("</font>\n");
        }
        if (!ObjectUtils.isEmpty(groupPayType)) {
            sb.append(">团购平台:<font color=\"warning\">").append(getGroupPayTypeStr(groupPayType)).append("</font>\n");
        }
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }


    @Override
    @Async
    public void sendCloseOrderMsg(Long storeId, Long userId, Long roomId, Integer payType, Integer groupPayType, String orderNo) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        String roomName = roomInfoMapper.getNameById(roomId);
        log.info("发送订单消息到配置的企业微信");
        StringBuffer sb = new StringBuffer();
        sb.append("用户提前结束订单通知\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">手机号码:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
        sb.append(">支付方式:<font color=\"warning\">").append(getPayTypeStr(payType)).append("</font>\n");
        if (!ObjectUtils.isEmpty(groupPayType)) {
            sb.append(">团购平台:<font color=\"warning\">").append(getGroupPayTypeStr(groupPayType)).append("</font>\n");
        }
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendGameMsg(Long storeId, String content) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getGameWebhook())) {
            return;
        }
        log.info("发送组局消息到配置的企业微信:{}", content);
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", content);
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getGameWebhook(), msg);
    }

    @Override
    @Async
    public void sendClearMsg(String webhookUrl, String content) {
        if (!ObjectUtils.isEmpty(webhookUrl)) {
            log.info("发送清洁消息到配置的企业微信:{}", content);
            JSONObject msg = new JSONObject();
            msg.put("msgtype", "text");
            JSONObject text = new JSONObject();
            text.put("content", content);
//            JSONArray mentioned_list = new JSONArray();
//            mentioned_list.add("@all");
//            text.put("mentioned_list", mentioned_list);
            msg.put("text", text);
            workWxClient.sendMDMsg(webhookUrl, msg);
        }

    }

    /**
     * @param storeId
     * @param userId
     * @param roomName
     * @param orderNo
     * @param endTime
     * @param isAdmin
     */
    @Override
    @Async
    public void sendRenewMsg(Long storeId, Long userId, String roomName, BigDecimal price, Integer payType, String orderNo, Date endTime, CouponInfoDO couponInfoDO, boolean isAdmin) {
//        String storeName=storeInfoMapper.getNameById(storeId);
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        String userName = appUserMapper.getNameById(userId);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        if (isAdmin) {
            sb.append("管理员续费通知\n");
        } else {
            sb.append("续费通知\n");
        }
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(userName).append("</font>\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
        if (!isAdmin) {
            sb.append(">支付方式:<font color=\"warning\">").append(getPayTypeStr(payType)).append("</font>\n");
//            sb.append(">续费金额:<font color=\"warning\">").append(price).append("</font>\n");
        }
        if (!ObjectUtils.isEmpty(couponInfoDO)) {
            sb.append(">使用卡券:<font color=\"warning\">").append(couponInfoDO.getCouponName()).append("</font>\n");
        }
        sb.append(">结束时间:<font color=\"warning\">").append(DateUtils.dateToStr(endTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        log.info("发送订单消息到配置的企业微信");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendRechargeMsg(Long storeId, Long userId, BigDecimal price, BigDecimal giftPrice) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        sb.append("用户充值通知\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">用户手机号:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">充值门店:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">充值金额:<font color=\"warning\">").append(price).append("</font>\n");
        sb.append(">赠送金额:<font color=\"warning\">").append(giftPrice).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendAdminRechargeMsg(Long storeId, Long userId, Long adminUserId, BigDecimal giftPrice) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        MemberUserDO adminMember = memberUserMapper.selectById(adminUserId);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        sb.append("管理员给用户充值通知\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">用户手机号:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">充值门店:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">赠送金额:<font color=\"warning\">").append(giftPrice).append("</font>\n");
        sb.append(">管理人员:<font color=\"warning\">").append(adminMember.getNickname()).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendGiftCouponMsg(Long storeId, Long userId, String couponName, Integer roomType) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        sb.append("管理员赠送卡券通知\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">用户手机号:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">适用门店:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">适用包间:<font color=\"warning\">").append(ObjectUtils.isEmpty(roomType) ? "不限" : getRoomTypeStr(roomType)).append("</font>\n");
        sb.append(">卡券名称:<font color=\"warning\">").append(couponName).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendMeiTuanScopeMsg(Long storeId) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        sb.append("美团授权需要更新通知\n");
        sb.append(">门店:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendChangeRoomMsg(Long storeId, String orderNo, Date startTime, Date endTime, String oldRoomName, String newRoomName, Long userId) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        sb.append("用户订单更换房间通知\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">原房间:<font color=\"warning\">").append(oldRoomName).append("</font>\n");
        sb.append(">新房间:<font color=\"warning\">").append(newRoomName).append("</font>\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
        sb.append(">开始时间:<font color=\"warning\">").append(DateUtils.dateToStr(startTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">结束时间:<font color=\"warning\">").append(DateUtils.dateToStr(endTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">用户手机号:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendOrderChangeUserMsg(Long storeId, String orderNo, Long roomId, Date startTime, Date endTime, Long userId) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        String roomName = roomInfoMapper.getNameById(roomId);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        sb.append("管理员订单转移通知\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">开始时间:<font color=\"warning\">").append(DateUtils.dateToStr(startTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">结束时间:<font color=\"warning\">").append(DateUtils.dateToStr(endTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">手机号码:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendUseGroupNoMsg(GroupPayInfoDO groupPayInfoDO, Long userId) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(groupPayInfoDO.getStoreId());
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        log.info("发送订单消息到配置的企业微信");
        StringBuffer sb = new StringBuffer();
        sb.append("管理员团购验券通知\n");
        sb.append(">券码名称:<font color=\"warning\">").append(groupPayInfoDO.getGroupName()).append("</font>\n");
        sb.append(">券码编号:<font color=\"warning\">").append(groupPayInfoDO.getGroupNo()).append("</font>\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">团购平台:<font color=\"warning\">").append(getGroupPayTypeStr(groupPayInfoDO.getGroupPayType())).append("</font>\n");
        sb.append(">销售价格:<font color=\"warning\">").append(groupPayInfoDO.getGroupPayPrice()).append("</font>\n");
        sb.append(">管理员:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendClearRoomMsg(Long storeId, Long roomId, Long userId, String type) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        String roomName = roomInfoMapper.getNameById(roomId);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        sb.append("管理员" + type + "通知\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendChangeMsg(Long storeId, String orderNo, Date startTime, Date endTime, Long oldRoom, Long newRoom, Long userId) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        String oldRoomName = roomInfoMapper.getNameById(oldRoom);
        String newRoomName = roomInfoMapper.getNameById(newRoom);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        sb.append("管理员修改订单通知\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        if (oldRoom.compareTo(newRoom) == 0) {
            //没有更换房间
            sb.append(">房间名称:<font color=\"warning\">").append(oldRoomName).append("</font>\n");
        } else {
            sb.append(">原房间名:<font color=\"warning\">").append(oldRoomName).append("</font>\n");
            sb.append(">新房间名:<font color=\"warning\">").append(newRoomName).append("</font>\n");
        }
        sb.append(">开始时间:<font color=\"warning\">").append(DateUtils.dateToStr(startTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">结束时间:<font color=\"warning\">").append(DateUtils.dateToStr(endTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendClearFinishMsg(Long storeId, Long roomId, Long userId, String type) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        String roomName = roomInfoMapper.getNameById(roomId);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        sb.append("保洁").append(type).append("通知\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">保洁员:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendRepeatOrderMsg(Long storeId, String roomName, Date startTime, Date endTime, Long userId) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        log.info("发送订单消息到配置的企业微信");
        StringBuffer sb = new StringBuffer();
        sb.append("用户重复下单同一房间通知\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">手机号码:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendOrderClearMsg(Long storeId, String roomName, Date startTime, Date endTime) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        log.info("发送订单消息到配置的企业微信");
        StringBuffer sb = new StringBuffer();
        sb.append("待清洁房间被下单通知\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">开始时间:<font color=\"warning\">").append(DateUtils.dateToStr(startTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append("><font color=\"warning\">").append("请注意清洁房间!").append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendOrderSubmitMsg(Long storeId, Long userId, String mobile, String roomName, String orderNo, Date startTime, Date endTime) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        log.info("发送订单消息到配置的企业微信");
        StringBuffer sb = new StringBuffer();
        sb.append("管理员代用户下单通知\n");
        sb.append(">管理员:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
        sb.append(">用户手机:<font color=\"warning\">").append(mobile).append("</font>\n");
        sb.append(">开始时间:<font color=\"warning\">").append(DateUtils.dateToStr(startTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">结束时间:<font color=\"warning\">").append(DateUtils.dateToStr(endTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendCallMsg(Long storeId, String tts) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        log.info("发送订单消息到配置的企业微信");
        StringBuffer sb = new StringBuffer();
        sb.append("顾客呼叫服务\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">呼叫内容:<font color=\"warning\">").append(tts).append("</font>\n");
        sb.append(">操作时间:<font color=\"warning\">").append(DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);

    }

    private String getPayTypeStr(Integer type) {
        switch (type) {
            case 1:
                return "微信";
            case 2:
                return "余额";
            case 3:
                return "团购";
            case 4:
                return "套餐";
        }
        return "";
    }

    private String getRoomTypeStr(Integer type) {
        switch (type) {
            case 1:
                return "小包";
            case 2:
                return "中包";
            case 3:
                return "大包";
            case 4:
                return "豪包";
            case 5:
                return "商务包";
        }
        return "";
    }

    private String getGroupPayTypeStr(Integer type) {
        switch (type) {
            case 1:
                return "美团";
            case 2:
                return "抖音";
            case 3:
                return "快手";
        }
        return "";
    }
}
