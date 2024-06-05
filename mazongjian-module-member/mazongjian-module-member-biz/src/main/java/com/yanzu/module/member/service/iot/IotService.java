package com.yanzu.module.member.service.iot;

import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

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

    @Resource
    private DeviceInfoMapper deviceInfoMapper;


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
     * 绑定设备
     */
    public String bind(String sn) {
        IotDeviceBaseVO reqVO = new IotDeviceBaseVO();
        reqVO.setDeviceSn(sn);
        reqVO.setTs(new Date().getTime());
        IotResult<String> resp = iotClient.bind(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return resp.getData();
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }
    }

    /**
     * 解绑设备
     */

    public Boolean unbind(String sn) {
        IotDeviceBaseVO reqVO = new IotDeviceBaseVO();
        reqVO.setDeviceSn(sn);
        reqVO.setTs(new Date().getTime());
        IotResult<Boolean> resp = iotClient.unbind(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return true;
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }
    }


    /**
     * 设备控制
     */
    public Boolean control(IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO) {
        reqVO.setTs(new Date().getTime());
        IotResult<Boolean> resp = iotClient.control(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return true;
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }

    }


    /**
     * 重置wifi
     */
    public Boolean configWifi(IotDeviceBaseVO<IotDeviceConfigWifiReqVO> reqVO) {
        reqVO.setTs(new Date().getTime());
        IotResult<Boolean> resp = iotClient.configWifi(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return true;
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }

    }


    public Boolean setLockAutoLock(IotDeviceSetAutoLockReqVO reqVO) {
        reqVO.setTs(new Date().getTime());
        IotResult<Boolean> resp = iotClient.setLockAutoLock(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return true;
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }
    }


    public void iotCallback(JSONObject json) {
        String type = json.getString("type");
        if (type.equals("online")) {
            //设备上线或下线消息
            deviceInfoMapper.updateStatusBySN(json.getString("sn"), json.getInteger("status"));
        }
    }
}
