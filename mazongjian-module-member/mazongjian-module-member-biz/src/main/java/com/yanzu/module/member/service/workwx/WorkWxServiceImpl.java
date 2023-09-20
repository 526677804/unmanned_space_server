package com.yanzu.module.member.service.workwx;

import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.forest.WorkWxClient;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
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
    public void sendMDMsg(Long storeId, String content) {
        //查询出webhook的地址
        StoreInfoDO storeInfoDO = storeInfoMapper.selectById(storeId);
        if (ObjectUtils.isEmpty(storeInfoDO) || ObjectUtils.isEmpty(storeInfoDO.getWxWebhook())) {
            return;
        }
        log.info("发送MD消息到配置的企业微信:{}", content);
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "markdown");
        JSONObject markdown = new JSONObject();
        markdown.put("content", content);

        msg.put("markdown", markdown);
        workWxClient.sendMDMsg(storeInfoDO.getWxWebhook(), msg);
    }
}
