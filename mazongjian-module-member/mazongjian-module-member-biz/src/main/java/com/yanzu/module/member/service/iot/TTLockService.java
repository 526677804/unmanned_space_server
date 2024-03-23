package com.yanzu.module.member.service.iot;

import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.forest.TTLockClient;
import com.yanzu.module.member.service.iot.ttlockBean.TTLockKeyReqVO;
import com.yanzu.module.member.service.iot.ttlockBean.TTLockTokenReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.iot
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2024/3/22 13:56
 */
@Slf4j
@Component
public class TTLockService {

    @Resource
    private TTLockClient ttLockClient;

    @Value("${ttlock.clientId}")
    private String clientId;

    @Value("${ttlock.clientSecret}")
    private String clientSecret;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    private final String redis_key = "ttlock.token";

    public String getToken() {
        if (stringRedisTemplate.hasKey(redis_key)) {
            return stringRedisTemplate.opsForValue().get(redis_key);
        } else {
            TTLockTokenReqVO reqVO = new TTLockTokenReqVO();
            reqVO.setClientId(clientId);
            reqVO.setClientSecret(clientSecret);
            JSONObject resp = ttLockClient.getToken(reqVO);
            String accessToken = resp.getString("access_token");
            stringRedisTemplate.opsForValue().set(redis_key, resp.getString("access_token"), resp.getLong("expires_in"), TimeUnit.SECONDS);
            return accessToken;
        }
    }

    public String getKey(Integer lockId) {
        String token = this.getToken();
        TTLockKeyReqVO reqVO = new TTLockKeyReqVO();
        reqVO.setAccessToken(token);
        reqVO.setLockId(lockId);
        reqVO.setDate(new Date().getTime());
        reqVO.setClientId(clientId);
        JSONObject key = ttLockClient.getKey(reqVO);
        log.info("{}钥匙信息查询结果:{}", lockId, key);
        return key.getString("lockData");
    }


}
