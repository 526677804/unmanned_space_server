package com.yanzu.module.member.service.iot;

import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.forest.IotDeviceClient;
import com.yanzu.module.member.service.iot.device.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DEVICE_IOT_AUTH_ERROR;
import static com.yanzu.module.member.enums.ErrorCodeConstants.DEVICE_IOT_OP_ERROR;

@Slf4j
@Component
public class IotDeviceService {

    @Value("${iot.clientId}")
    private String clientId;
    @Value("${iot.secret}")
    private String secret;
    @Value("${iot.redirectUrl}")
    private String redirectUrl;

    @Resource
    private IotDeviceClient iotDeviceClient;

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
        IotResult authorize = iotDeviceClient.authorize(reqVO);
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
        IotResult<String> resp = iotDeviceClient.bind(reqVO, clientId, secret);
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
        IotResult<Boolean> resp = iotDeviceClient.unbind(reqVO, clientId, secret);
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
        IotResult<Boolean> resp = iotDeviceClient.control(reqVO, clientId, secret);
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
        IotResult<Boolean> resp = iotDeviceClient.configWifi(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return true;
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }

    }


    public Boolean setLockAutoLock(IotDeviceSetAutoLockReqVO reqVO) {
        reqVO.setTs(new Date().getTime());
        IotResult<Boolean> resp = iotDeviceClient.setLockAutoLock(reqVO, clientId, secret);
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
