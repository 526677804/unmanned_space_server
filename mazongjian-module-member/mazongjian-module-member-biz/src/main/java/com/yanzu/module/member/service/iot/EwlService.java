package com.yanzu.module.member.service.iot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.forest.EwlClient;
import com.yanzu.module.member.service.iot.ewlbean.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
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

    private String appid = "iSQwkQ8PfVRB75jaOgsPJtwfAdG1678f";

    private String secret = "y0guc89GZnkcbbRDRzOL3HCh7LaXNpgw";


    private String redirectUrl = "https://api-dev.scyanzu.com";

    private String grantType = "authorization_code";

    private String state = "state";


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
        System.out.println(url);
        return url.toString();
    }

    public String getToken(String code, String state) {
        return "28e3579792d46a0e00e323d0ced05d9e97691782";
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

    public void getThing() {
        //apikey -> f3bfc723-56e6-4f98-a69f-1a1ab94f3e87
        EwlGetThingReqVO reqVO = new EwlGetThingReqVO();
        EwlBaseRespVO<JSONObject> thing = ewlClient.getThing(reqVO, appid, "28e3579792d46a0e00e323d0ced05d9e97691782");
        if (thing.getError() == 0) {
            JSONArray thingList = thing.getData().getJSONArray("thingList");
            thingList.forEach(v->{
                JSONObject jsonObject = (JSONObject) v;
                JSONObject itemData = jsonObject.getJSONObject("itemData");
                String name=itemData.getString("name");
                String deviceid=itemData.getString("deviceid");
                String apiKey=itemData.getString("apiKey");
                JSONObject extra = itemData.getJSONObject("extra");
                String ui=extra.getString("ui");
                log.info("name:{},deviceid:{},apiKey:{},ui:{}",name,deviceid,apiKey,ui);
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


}
