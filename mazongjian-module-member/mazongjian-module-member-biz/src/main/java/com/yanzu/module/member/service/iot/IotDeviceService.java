package com.yanzu.module.member.service.iot;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.JSONBody;
import com.yanzu.framework.tenant.core.util.TenantUtils;
import com.yanzu.module.member.controller.app.store.vo.AppAddLockReqVO;
import com.yanzu.module.member.dal.dataobject.facerecord.FaceRecordDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.facerecord.FaceRecordMapper;
import com.yanzu.module.member.forest.IotClient;
import com.yanzu.module.member.forest.IotDeviceClient;
import com.yanzu.module.member.service.iot.device.*;
import com.yanzu.module.member.service.iot.platform.IotPushDataReqVO;
import com.yanzu.module.member.service.wx.WorkWxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
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

    @Resource
    private WorkWxService workWxService;

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
     *
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
     *
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
                IotDeviceRoomInfoVO deviceRoomVO = null;
                switch (type) {
                    case "online":
                        //设备上线/下线
                        deviceInfoMapper.updateStatusBySN(data.getString("deviceSn"), data.getInteger("status"));
                        break;
                    case "face_record":
                        //人脸识别记录回调
                        //查找出设备
                        deviceRoomVO = deviceInfoMapper.getDeviceRoomVO(data.getString("deviceSn"));
                        //把照片url转成base64编码
                        String base64Image = convertImageToBase64(data.getString("photoUrl"));
                        FaceRecordDO faceRecordDO = new FaceRecordDO()
                                .setStoreId(deviceRoomVO.getStoreId())
                                .setFaceId(data.getString("faceId"))
                                .setDeviceSn(data.getString("deviceSn"))
                                .setAdmitGuid(data.getString("admitGuid"))
                                .setPhotoUrl(data.getString("photoUrl"))
                                .setPhotoData(base64Image)
                                .setShowTime(new Date(data.getLong("showTime")))
                                .setType(data.getInteger("type"));
                        //模拟租户
                        TenantUtils.execute(deviceRoomVO.getTenantId(), () -> {
                            faceRecordMapper.insert(faceRecordDO);
                        });
                        break;
                    case "call":
                        //客户呼叫
                        callTask(data);
                        break;
                }
            } else {
                log.error("签名不匹配,{}", sign);
            }
        }
    }




    /**
     * 顾客呼叫处理
     * @param data
     */
    private void callTask(JSONObject data){
        String deviceSn = data.getString("deviceSn");
        //通过设备编号找出该设备所在门店的喇叭编号
        IotDeviceRoomInfoVO deviceRoomVO = deviceInfoMapper.getDeviceRoomVO(deviceSn);
        String roomName = ObjectUtils.isEmpty(deviceRoomVO.getRoomName()) ? "" : deviceRoomVO.getRoomName();
        String callType = data.getString("callType");
        if (!ObjectUtils.isEmpty(deviceRoomVO)) {
            //门店有绑定喇叭才处理
            //模拟租户
            TenantUtils.execute(deviceRoomVO.getTenantId(), () -> {
                List<IotDeviceRoomInfoVO> storeVoiceList = deviceInfoMapper.getStoreVoice(deviceRoomVO.getStoreId());
                if (!CollectionUtils.isEmpty(storeVoiceList)) {
                    String tts = getTTSByCallType(callType, roomName);
                    storeVoiceList.forEach(x -> {
                        //重复三次
                        for (int i = 0; i < 3; i++) {
                            runSound(x.getDeviceSn(), tts);
                        }
                    });
                    //再异步发送企业微信通知
                    workWxService.sendCallMsg(deviceRoomVO.getStoreId(), tts);
                }
            });
        }
    }

    public String convertImageToBase64(String imageUrl) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建 GET 请求
            HttpGet request = new HttpGet(imageUrl);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            // 获取图片的字节数据
            if (entity != null) {
                byte[] imageBytes = EntityUtils.toByteArray(entity);
                String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
                return base64Image;
            }
            return "Error: Image not found!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String getTTSByCallType(String callType, String roomName) {
        String tts = "";
        switch (callType) {
            case "CALL1":
                //呼叫服务员
                tts = roomName + ",顾客,呼叫服务员";
                break;
            case "CALL2":
                //需要换零钱
                tts = roomName + ",顾客,需要换零钱";
                break;
            case "CALL3":
                //需要购买商品
                tts = roomName + ",顾客,需要购买商品";
                break;
            case "CALL4":
                //需要加水
                tts = roomName + ",顾客,需要加水";
                break;
            case "CALL5":
                //需要清洁
                tts = roomName + ",顾客,需要清洁";
                break;
            case "CALL6":
                //需要点餐
                tts = roomName + ",顾客,需要点餐";
                break;
            case "CALL7":
                //需要换现金
                tts = roomName + ",顾客,需要换现金";
                break;
            case "BTN_ON":
                //呼叫服务员
                tts = roomName + ",顾客,呼叫服务员";
                break;
        }
        return tts;
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

    /**
     * 控制空调
     */
    public Boolean controlKT(IotControlKTReqVO req) {
        IotResult<Boolean> resp = iotDeviceClient.controlKT(req, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return true;
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }

    }

    /**
     * 添加智能锁
     * @param reqVO
     */
    public Boolean addLock(AppAddLockReqVO reqVO) {
        IotResult<IotAddLockRespVO> resp = iotDeviceClient.addLock(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return true;
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }
    }
}
