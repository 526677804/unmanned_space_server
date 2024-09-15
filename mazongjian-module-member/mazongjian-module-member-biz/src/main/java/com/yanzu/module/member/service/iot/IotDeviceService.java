package com.yanzu.module.member.service.iot;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.forest.IotDeviceClient;
import com.yanzu.module.member.service.iot.device.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import sun.security.provider.MD5;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

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

    private void runSound(String sn, String cmd) {
        IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO = new IotDeviceBaseVO();
        List<IotDeviceContrlReqVO> param = new ArrayList<>(1);
        IotDeviceContrlReqVO iotDeviceContrlReqVO = new IotDeviceContrlReqVO();
        iotDeviceContrlReqVO.setOutlet(0).setCmd(cmd);
        param.add(iotDeviceContrlReqVO);
        reqVO.setDeviceSn(sn).setParams(param);
        control(reqVO);
    }

    public void iotPlatform(JSONObject json) {
        if (json.containsKey("sign") && json.containsKey("type") && json.containsKey("t")) {
            //先校验签名
            String sign = json.getString("sign");
            String type = json.getString("type");
            long t = json.getLong("t");
            String newSign = SecureUtil.md5(secret + t);
            if (newSign.equals(sign)) {
                JSONObject data = json.getJSONObject("data");
                //根据数据类型  进行不同的处理
                switch (type) {
                    case "online":
                        //设备上线/下线
                        deviceInfoMapper.updateStatusBySN(data.getString("deviceSn"), data.getInteger("status"));
                        break;
                    case "face_record":
                        //人脸识别记录回调

                        break;
                    case "call":
                        //客户呼叫
                        String deviceSn = data.getString("deviceSn");
                        IotDeviceRoomInfoVO deviceRoomVO = deviceInfoMapper.getDeviceRoomVO(deviceSn);
                        if (!ObjectUtils.isEmpty(deviceRoomVO)) {
                            String roomName = ObjectUtils.isEmpty(deviceRoomVO.getRoomName()) ? "" : deviceRoomVO.getRoomName();
                            String callType = data.getString("callType");
                            //指定房间的音箱进行语音播放
                            switch (callType) {
                                case "CALL1":
                                    //呼叫服务员
                                    runSound(deviceSn, roomName + ",顾客,呼叫服务员");
                                    break;
                                case "CALL2":
                                    //需要换零钱
                                    runSound(deviceSn, roomName + ",顾客,需要换零钱");
                                    break;
                                case "CALL3":
                                    //需要购买商品
                                    runSound(deviceSn, roomName + ",顾客,需要购买商品");
                                    break;
                                case "CALL4":
                                    //需要加水
                                    runSound(deviceSn, roomName + ",顾客,需要加水");
                                    break;
                                case "CALL5":
                                    //需要清洁
                                    runSound(deviceSn, roomName + ",顾客,需要清洁");
                                    break;
                                case "BTN_ON":
                                    //呼叫服务员
                                    runSound(deviceSn, roomName + ",顾客,呼叫服务员");
                                    break;
                            }
                        }
                        break;
                }

            } else {
                //签名不匹配
                log.error("回调请求的签名不匹配");
            }
        }


    }
}
