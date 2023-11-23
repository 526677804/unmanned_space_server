package com.yanzu.module.member.service.iot.ewlbean;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot.ewlbean
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/23 18:08
 */
@Data
public class EwlHandReqVO {


    private String action = "userOnline";//固定参数: userOnline
    private String at;//登录接口获取的 AT
    private String apikey;//用户 apikey（可从登陆接口获取）
    private String appid;//APPID
    private String nonce;//8 位字母数字随机数
    private long ts;//时间戳精确到秒
    private String userAgent = "app";//固定参数: app
    private String sequence;//时间戳精确到毫秒
    private Integer version = 8;//接口版本: 8


    public EwlHandReqVO() {
        Date now = new Date();
        this.ts = now.getTime();
        this.sequence = String.valueOf(now.getTime());
        this.nonce = UUID.randomUUID().toString().substring(0, 8);
    }
}
