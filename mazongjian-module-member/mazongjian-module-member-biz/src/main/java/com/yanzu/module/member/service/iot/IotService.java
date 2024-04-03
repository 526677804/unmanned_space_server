package com.yanzu.module.member.service.iot;

import com.yanzu.module.member.forest.IotClient;
import com.yanzu.module.member.service.iot.iotBean.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DEVICE_IOT_AUTH_ERROR;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DEVICE_IOT_OP_ERROR;

@Slf4j
@Component
public class IotService {

    @Value("${iot.clientId}")
    private String clientId;
    @Value("${iot.secret}")
    private String secret;
    @Value("${iot.redirectUrl}")
    private String redirectUrl;

    @Resource
    private IotClient iotClient;

    @Resource
    private RedisTemplate redisTemplate;

    private final String tokenKey = "iot.token";
    private final String refushTokenKey = "iot.refush_token";
    private final String tokenExpireTimeKey = "iot.tokenExpireTime";

    /**
     * 发起授权
     */
    public void authorize() {
        IotAuthReqVO reqVO = new IotAuthReqVO();
        reqVO.setClient_id(clientId);
        reqVO.setSecret(secret);
        reqVO.setRedirect_uri(redirectUrl);
        IotResult authorize = iotClient.authorize(reqVO);
        if (authorize.getCode().intValue() != 0) {
            throw exception(DEVICE_IOT_AUTH_ERROR);
        }
    }

    /**
     * 获取token
     */
    public String getToken(String code) {
        //获取
        IotResult<IotTokenRespVO> token = iotClient.getToken(new IotTokenReqVO().setClient_id(clientId).setSecret(secret).setCode(code));
        if (token.getCode().intValue() == 0) {
            redisTemplate.opsForValue().set(tokenKey, token.getData().getAccess_token());
            redisTemplate.opsForValue().set(refushTokenKey, token.getData().getRefresh_token());
            redisTemplate.opsForValue().set(tokenExpireTimeKey, token.getData().getExpires_in());
            return token.getData().getAccess_token();
        } else {
            log.error("硬件平台获取token失败:{}", token.getMsg());
            return null;
        }

    }

    private String getToken() {
        return (String) redisTemplate.opsForValue().get(tokenKey);
    }

    /**
     * 刷新token
     */
    private void getTokenRefush(String token) {
        //获取
        IotResult<IotTokenRespVO> resp = iotClient.getTokenRefush(new IotTokenRefushReqVO().setClient_id(clientId).setSecret(secret).setToken(token));
        if (resp.getCode().intValue() == 0) {
            redisTemplate.opsForValue().set(tokenKey, resp.getData().getAccess_token());
            redisTemplate.opsForValue().set(refushTokenKey, resp.getData().getRefresh_token());
            redisTemplate.opsForValue().set(tokenExpireTimeKey, resp.getData().getExpires_in());
        } else {
            log.error("硬件平台刷新token授权失败:{}", resp.getMsg());
        }

    }

    /**
     * 绑定设备
     */

    public Boolean bind(String sn) {
        IotDeviceBaseVO reqVO = new IotDeviceBaseVO();
        reqVO.setDeviceSn(sn);
        reqVO.setTs(new Date().getTime());
        IotResult<Boolean> resp = iotClient.bind(reqVO, getToken());
        if(resp.getCode().intValue()==0){
            return true;
        }else{
            throw exception(DEVICE_IOT_OP_ERROR,resp.getMsg());
        }
    }

    /**
     * 解绑设备
     */

    public Boolean unbind(String sn) {
        IotDeviceBaseVO reqVO = new IotDeviceBaseVO();
        reqVO.setDeviceSn(sn);
        reqVO.setTs(new Date().getTime());
        IotResult<Boolean> resp = iotClient.unbind(reqVO, getToken());
        if(resp.getCode().intValue()==0){
            return true;
        }else{
            throw exception(DEVICE_IOT_OP_ERROR,resp.getMsg());
        }
    }


    /**
     * 设备控制
     */
    public Boolean control(IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO) {
        reqVO.setTs(new Date().getTime());
        IotResult<Boolean> resp = iotClient.control(reqVO, getToken());
        if(resp.getCode().intValue()==0){
            return true;
        }else{
            throw exception(DEVICE_IOT_OP_ERROR,resp.getMsg());
        }

    }


    public void refushTokenCheck() {
        String token = getToken();
        if (!ObjectUtils.isEmpty(token) && token.length() > 5) {
            String refushToken = (String) redisTemplate.opsForValue().get(refushTokenKey);
            Long tokenExpireTime = (Long) redisTemplate.opsForValue().get(tokenExpireTimeKey);
            log.info("refushToken:{}", refushToken);
            log.info("tokenExpireTime:{}", tokenExpireTime);
            LocalDateTime now = LocalDateTime.now();
            now = now.plusDays(2);//加2天  用来提前判断过期
            //时间戳转日期
            Instant instant = Instant.ofEpochMilli(Long.valueOf(tokenExpireTime)); // 将时间戳转换为Instant对象
            LocalDateTime t1 = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
            if (t1.isBefore(now)) {
                //过期了 需要刷新
                //换取新的token
                getTokenRefush(token);
            }
        }

    }
}
