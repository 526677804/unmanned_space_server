package com.yanzu.module.member.service.workwx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzu.framework.common.util.date.DateUtils;
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
    public void sendOrderMsg(Long storeId, Long userId,String roomName,BigDecimal price,Integer payType,String orderNo,Date startTime,Date endTime) {
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
        sb.append(">订单金额:<font color=\"warning\">").append(price).append("</font>\n");
        sb.append(">支付方式:<font color=\"warning\">").append(getPayTypeStr(payType)).append("</font>\n");
        sb.append(">开始时间:<font color=\"warning\">").append(DateUtils.dateToStr(startTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>\n");
        sb.append(">结束时间:<font color=\"warning\">").append(DateUtils.dateToStr(endTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    @Async
    public void sendOrderCancelMsg(Long storeId, Long userId,Long roomId,BigDecimal price,Integer payType,String orderNo) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        MemberUserDO memberUserDO = memberUserMapper.selectById(userId);
        String roomName = roomInfoMapper.getNameById(roomId);
        log.info("发送订单消息到配置的企业微信");
        StringBuffer sb = new StringBuffer();
        sb.append("订单取消通知\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(memberUserDO.getNickname()).append("</font>\n");
        sb.append(">手机号码:<font color=\"warning\">").append(memberUserDO.getMobile()).append("</font>\n");
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
        sb.append(">订单金额:<font color=\"warning\">").append(price).append("</font>\n");
        sb.append(">支付方式:<font color=\"warning\">").append(getPayTypeStr(payType)).append("</font>");
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
    public void sendClearMsg(String webhookUrl, String content) {
        if (!ObjectUtils.isEmpty(webhookUrl)) {
            log.info("发送清洁消息到配置的企业微信:{}", content);
            JSONObject msg = new JSONObject();
            msg.put("msgtype", "text");
            JSONObject text = new JSONObject();
            text.put("content", content);
            JSONArray mentioned_list = new JSONArray();
            mentioned_list.add("@all");
            text.put("mentioned_list", mentioned_list);
            msg.put("text", text);
            workWxClient.sendMDMsg(webhookUrl, msg);
        }

    }

    /**
     *
     * @param storeId
     * @param userId
     * @param roomName
     * @param orderNo
     * @param endTime
     * @param isAdmin
     */
    @Override
    public void sendRenewMsg(Long storeId, Long userId, String roomName, BigDecimal price,Integer payType, String orderNo, Date endTime,boolean isAdmin) {
//        String storeName=storeInfoMapper.getNameById(storeId);
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        String userName = appUserMapper.getNameById(userId);
        //异步发送微信通知
        StringBuffer sb = new StringBuffer();
        if(isAdmin){
            sb.append("管理员续费通知\n");
        }else{
            sb.append("续费通知\n");
        }
        sb.append(">门店名称:<font color=\"warning\">").append(storeInfoDO.getStoreName()).append("</font>\n");
        sb.append(">房间名称:<font color=\"warning\">").append(roomName).append("</font>\n");
        sb.append(">用户昵称:<font color=\"warning\">").append(userName).append("</font>\n");
        sb.append(">订单编号:<font color=\"warning\">").append(orderNo).append("</font>\n");
        if(!isAdmin){
            sb.append(">支付方式:<font color=\"warning\">").append(getPayTypeStr(payType)).append("</font>\n");
            sb.append(">续费金额:<font color=\"warning\">").append(price).append("</font>\n");
        }
        sb.append(">结束时间:<font color=\"warning\">").append(DateUtils.dateToStr(endTime, DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)).append("</font>");
        log.info("发送订单消息到配置的企业微信");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
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
        sb.append(">赠送金额:<font color=\"warning\">").append(giftPrice).append("</font>");
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", sb.toString());
        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getOrderWebhook(), msg);
    }

    @Override
    public void sendGiftCouponMsg(Long storeId, Long userId, String couponName) {
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
        sb.append(">卡券名称:<font color=\"warning\">").append(couponName).append("</font>\n");
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
        }
        return "";
    }
}
