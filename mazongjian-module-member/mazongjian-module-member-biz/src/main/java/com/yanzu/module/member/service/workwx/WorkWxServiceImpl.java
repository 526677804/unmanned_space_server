package com.yanzu.module.member.service.workwx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.forest.WorkWxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Service
@Validated
@Slf4j
public class WorkWxServiceImpl implements WorkWxService {

    @Autowired
    private WorkWxClient workWxClient;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Override
    @Async
    public void sendOrderMsg(Long storeId, String content) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getOrderWebhook())) {
            return;
        }
        log.info("发送订单消息到配置的企业微信:{}", content);
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", content);
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
}
