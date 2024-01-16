package com.yanzu.module.member.service.iot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.infra.api.config.ConfigApi;
import com.yanzu.module.member.forest.EwlClient;
import com.yanzu.module.member.service.iot.ewlbean.*;
import com.yanzu.module.member.service.wx.WorkWxService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/23 12:13
 */
@Slf4j
@Component
public class EwlService {
    @Resource
    private EwlClient ewlClient;
    @Resource
    private WorkWxService workWxService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ConfigApi configApi;

    @Value("${ewelink.appid}")
    private String appid;
    @Value("${ewelink.secret}")
    private String secret;
    @Value("${ewelink.apiKey}")
    private String apiKey;
    @Value("${ewelink.redirectUrl}")
    private String redirectUrl;

    //    @Value("${ewelink.token:1}")
//    private String token;
//    @Value("${ewelink.refushToken:1}")
//    private String refushToken;
//    @Value("${ewelink.rtExpiredTime:1}")
//    private String rtExpiredTime;
//    @Value("${ewelink.atExpiredTime:1}")
//    private String atExpiredTime;
    private String grantType = "authorization_code";
    private String state = "state";

    @Resource
    private MyWebSocketClient myWebSocketClient;

    public String getAuthUrl() {
        Date now = new Date();
        StringBuffer url = new StringBuffer("https://c2ccdn.coolkit.cc/oauth/index.html?");
        url.append("state=").append(state);
        url.append("&clientId=").append(appid);
        url.append("&seq=").append(now.getTime());
        url.append("&redirectUrl=").append(redirectUrl);
        url.append("&nonce").append(UUID.randomUUID().toString().substring(0, 8));
        String authorization = HMACSHA256(appid + "_" + now.getTime());
        url.append("&authorization=").append(authorization);
        url.append("&grantType=").append(grantType);
        return url.toString();
    }

    public String getSocketUrl() {
        // 获取WebSocket连接地址
        JSONObject socketUrl = ewlClient.getSocketUrl();
        String serverUrl = "";
        if (socketUrl != null && socketUrl.getInteger("error") == 0) {
            serverUrl = String.format("wss://%s:%s/api/ws", socketUrl.getString("IP"), socketUrl.getString("port"));
        } else {
            //默认给一个地址
            serverUrl = "wss://52.80.9.35:8080/api/ws";
        }
        return serverUrl;
    }


    public String getToken() {
        return stringRedisTemplate.opsForValue().get("ewelink.token");
       /* EwlTokenReqVO reqVO = new EwlTokenReqVO();
        reqVO.setCode(code);
        reqVO.setRedirectUrl(redirectUrl);
        String jsonString = JSONObject.toJSONString(reqVO);
        EwlBaseRespVO<EwlTokenRespVO> token = ewlClient.getToken(reqVO, appid, HMACSHA256(jsonString));
        if (token.getError().compareTo(0) == 0) {
            //存起来  token 1个月过期 rtoken 3个月过期
//            accessToken -> 28e3579792d46a0e00e323d0ced05d9e97691782
            return "28e3579792d46a0e00e323d0ced05d9e97691782";
        }
        System.out.println(1);*/

    }

    public void setToken(String code, String state) {
        EwlTokenReqVO reqVO = new EwlTokenReqVO();
        reqVO.setCode(code);
        reqVO.setRedirectUrl(redirectUrl);
        String jsonString = JSONObject.toJSONString(reqVO);
        EwlBaseRespVO<EwlTokenRespVO> token = ewlClient.getToken(reqVO, appid, HMACSHA256(jsonString));
        log.info("token:{}",token);
        if (token.getError().compareTo(0) == 0) {
            //存起来  token 1个月过期 rtoken 3个月过期
//            accessToken -> 28e3579792d46a0e00e323d0ced05d9e97691782
            configApi.updateConfigValue("ewelink.token", token.getData().getAccessToken());
            configApi.updateConfigValue("ewelink.refushToken", token.getData().getRefreshToken());
            configApi.updateConfigValue("ewelink.rtExpiredTime", String.valueOf(token.getData().getRtExpiredTime()));
            configApi.updateConfigValue("ewelink.atExpiredTime", String.valueOf(token.getData().getAtExpiredTime()));
            myWebSocketClient.init();
        }
        //输出设备列表，仅为了获取apikey
        getThing();
    }

    public void getThing() {
        //apikey -> f3bfc723-56e6-4f98-a69f-1a1ab94f3e87
        String token = getToken();
        EwlGetThingReqVO reqVO = new EwlGetThingReqVO();
        EwlBaseRespVO<JSONObject> thing = ewlClient.getThing(reqVO, appid, token);
        log.info("=========== 易微联设备列表:");
        if (thing.getError() == 0) {
            JSONArray thingList = thing.getData().getJSONArray("thingList");
            thingList.forEach(v -> {
                JSONObject jsonObject = (JSONObject) v;
                JSONObject itemData = jsonObject.getJSONObject("itemData");
                String name = itemData.getString("name");
                String deviceid = itemData.getString("deviceid");
                String apikey = itemData.getString("apikey");
                JSONObject extra = itemData.getJSONObject("extra");
                String ui = extra.getString("ui");
                String uiid = extra.getString("uiid");
                log.info("apiKey:{},name:{},deviceid:{},uiid:{},ui:{}", apikey,name, deviceid,  uiid, ui);
            });
        } else {
            log.error("获取eweilink设备列表失败！data:{}", thing);
        }
    }


//    public void login() {
//        EwlUserLoginReqVO reqVO = new EwlUserLoginReqVO();
////        String sign = sortAndConcatenateParameters(reqVO);
//        String jsonString = JSONObject.toJSONString(reqVO);
//        EwlBaseRespVO<EwlUserLoginRespVO> login = ewlClient.login(reqVO, appid, HMACSHA256(jsonString));
//
//        System.out.println(login);
//
//
//    }


    @SneakyThrows
    private String HMACSHA256(String data) {
        // 创建一个 HMAC SHA256 的实例
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        // 创建一个 SecretKeySpec 对象，并使用密钥初始化 HMAC 实例
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secretKeySpec);
        // 计算消息的摘要
        byte[] digest = hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        // 使用 Base64 编码输出摘要
        String base64Digest = Base64.getEncoder().encodeToString(digest);
        return base64Digest;
    }

    private String sortAndConcatenateParameters(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        StringBuilder result = new StringBuilder();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    if (result.length() > 0) {
                        result.append("&");
                    }
                    result.append(field.getName()).append("=").append(value.toString());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 检查并刷新token
     */
    public void refushTokenCheck() {
        String token = getToken();
        if (!ObjectUtils.isEmpty(token) && token.length() > 5) {
            String refushToken = stringRedisTemplate.opsForValue().get("ewelink.refushToken");
            String atExpiredTime = stringRedisTemplate.opsForValue().get("ewelink.atExpiredTime");
            String rtExpiredTime = stringRedisTemplate.opsForValue().get("ewelink.rtExpiredTime");
            log.info("ewelink.atExpiredTime:{}", atExpiredTime);
            log.info("ewelink.rtExpiredTime:{}", rtExpiredTime);
            LocalDateTime now = LocalDateTime.now();
            now = now.plusDays(2);//加2天  用来判断过期
            //时间戳转日期
            Instant instant = Instant.ofEpochMilli(Long.valueOf(atExpiredTime)); // 将时间戳转换为Instant对象
            LocalDateTime t1 = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
            if (t1.isBefore(now)) {
                //过期了 需要刷新  再判断一下刷新token的时间
                Instant instant2 = Instant.ofEpochMilli(Long.valueOf(rtExpiredTime)); // 将时间戳转换为Instant对象
                LocalDateTime t2 = LocalDateTime.ofInstant(instant2, ZoneId.of("UTC"));
                if (t2.isBefore(now)) {
                    //刷新token也过期了  提醒重新授权
                    workWxService.sendEwelinkRefushTokenMsg();
                } else {
                    //换取新的token
                    EwlRefreshTokenReqVO reqVO = new EwlRefreshTokenReqVO(refushToken);
                    String sign = sortAndConcatenateParameters(reqVO);
                    String jsonString = JSONObject.toJSONString(reqVO);
                    JSONObject refresh = ewlClient.refresh(reqVO, appid, HMACSHA256(jsonString));
                    log.info("refresh result:{}", refresh);
                    if (refresh.getInteger("error") == 0) {
                        configApi.updateConfigValue("ewelink.token", refresh.getString("at"));
                        configApi.updateConfigValue("ewelink.refushToken", refresh.getString("rt"));
                        //重新连接
                        myWebSocketClient.init();
                    } else {
                        workWxService.sendEwelinkRefushTokenMsg();
                    }
                }
            }

        }
    }

    public boolean runKongkai(String sn, String cmd) {
        JSONObject data = new JSONObject();
        data.put("action", "update");
        data.put("deviceid", sn);
        data.put("apikey", apiKey);
        data.put("userAgent", "app");
        data.put("sequence", new Date().getTime() + "");
        JSONObject params = new JSONObject();
        JSONArray switches = new JSONArray();
        JSONObject v = new JSONObject();
        v.put("switch", cmd);
        v.put("outlet", 0);
        switches.add(v);
        params.put("switches", switches);
        data.put("params", params);
        myWebSocketClient.sendToServer(JSON.toJSONString(data));
        return true;
    }
}
