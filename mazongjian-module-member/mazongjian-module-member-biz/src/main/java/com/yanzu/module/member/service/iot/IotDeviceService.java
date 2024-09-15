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

    /**
     * 旧版本回调接收  此回调方式即将下线
     * @param json
     */
    public void iotCallback(JSONObject json) {
        String type = json.getString("type");
        if (type.equals("online")) {
            //设备上线或下线消息
            deviceInfoMapper.updateStatusBySN(json.getString("sn"), json.getInteger("status"));
        }
    }

    /**
     * 新版本回调接收 建议用此方式
     * @param json
     */
    public void iotPlatform(JSONObject json) {
        if (json.containsKey("type") && json.containsKey("t") && json.containsKey("sign")) {
            String type = json.getString("type");
            String sign = json.getString("sign");
            long t = json.getLong("t");
            //签名校验
            String newSign = SecureUtil.md5(secret + t);
            if (newSign.equals(sign)) {
                JSONObject data = json.getJSONObject("data");
                switch (type) {
                    case "online":
                        //设备上线/下线
                        deviceInfoMapper.updateStatusBySN(data.getString("deviceSn"), data.getInteger("status"));
                        break;
                    case "face_record":
                        //人脸识别记录回调
                        FaceRecordDO faceRecordDO = new FaceRecordDO()
                                .setFaceId(data.getString("faceId"))
                                .setDeviceSn(data.getString("deviceSn"))
                                .setAdmitGuid(data.getString("admitGuid"))
                                .setPhotoUrl(data.getString("photoUrl"))
                                .setShowTime(new Date(data.getLong("photoUrl")))
                                .setType(data.getInteger("type"));
                        faceRecordMapper.insert(faceRecordDO);
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
                log.error("签名不匹配,{}", sign);
            }
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
}
