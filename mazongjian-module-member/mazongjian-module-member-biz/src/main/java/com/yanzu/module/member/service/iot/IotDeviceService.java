package com.yanzu.module.member.service.iot;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.JSONBody;
import com.yanzu.module.member.dal.dataobject.facerecord.FaceRecordDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.facerecord.FaceRecordMapper;
import com.yanzu.module.member.forest.IotClient;
import com.yanzu.module.member.forest.IotDeviceClient;
import com.yanzu.module.member.service.iot.device.*;
import com.yanzu.module.member.service.iot.platform.IotPushDataReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
    private IotClient iotClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private FaceRecordMapper faceRecordMapper;

    public void online() {
        if (!ObjectUtils.isEmpty(redirectUrl) && redirectUrl.startsWith("https://")) {
            JSONObject data = new JSONObject();
            data.put("redirectUrl", redirectUrl);
            data.put("clientId", clientId);
            IotPushDataReqVO iotPushDataReqVO = new IotPushDataReqVO()
                    .setType("online")
                    .setData(data);
            IotResult<JSONBody> result = iotClient.pushData(iotPushDataReqVO, clientId, secret);
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


    public String addUserFace(Long storeId, String photoUrl, String remark) {
        IotDeviceAddBlacklistReqVO reqVO = new IotDeviceAddBlacklistReqVO()
                .setStoreId(storeId)
                .setPhotoUrl(photoUrl)
                .setRemark(remark);
        IotResult<String> resp = iotDeviceClient.addBlacklist(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return resp.getData();
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }

    }

    public void delUserFace(Long storeId, String admitGuid) {
        IotDeviceDelBlacklistReqVO reqVO = new IotDeviceDelBlacklistReqVO()
                .setStoreId(storeId)
                .setAdmitGuid(admitGuid);
        IotResult<Boolean> resp = iotDeviceClient.delBlacklist(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {

        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }
    }

    public void iotCallback(JSONObject json) {
        if (!json.containsKey("type") && !json.containsKey("t") && !json.containsKey("sign")) {
            String type = json.getString("type");
            String sign = json.getString("sign");
            long t = json.getLong("t");
            //签名校验
            String newSign = SecureUtil.md5(secret + t);
            if (newSign.equals(sign)) {
                JSONObject data = json.getJSONObject("data");
                if (type.equals("online")) {
                    //设备上线或下线消息
                    deviceInfoMapper.updateStatusBySN(data.getString("sn"), data.getInteger("status"));
                } else if (type.equals("face_record")) {
                    //人脸识别记录
                    FaceRecordDO faceRecordDO = new FaceRecordDO()
                            .setFaceId(data.getString("faceId"))
                            .setDeviceSn(data.getString("deviceSn"))
                            .setAdmitGuid(data.getString("admitGuid"))
                            .setPhotoUrl(data.getString("photoUrl"))
                            .setShowTime(new Date(data.getLong("photoUrl")))
                            .setType(data.getInteger("type"));
                    faceRecordMapper.insert(faceRecordDO);
                }
            } else {
                log.error("签名不匹配,{}", sign);
            }
        }

    }


}
