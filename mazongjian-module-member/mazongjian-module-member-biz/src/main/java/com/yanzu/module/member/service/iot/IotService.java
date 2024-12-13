package com.yanzu.module.member.service.iot;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.JSONBody;
import com.yanzu.framework.tenant.core.aop.TenantIgnore;
import com.yanzu.framework.tenant.core.util.TenantUtils;
import com.yanzu.module.member.controller.app.order.vo.OrderSaveReqVO;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.reserve.vo.TimePeriodItemsSub;
import com.yanzu.module.member.controller.app.reserve.vo.UpdateStockReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppAddLockReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppRoomListVO;
import com.yanzu.module.member.dal.dataobject.facerecord.FaceRecordDO;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.facerecord.FaceRecordMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.forest.IotClient;
import com.yanzu.module.member.forest.IotDeviceClient;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.iot.device.*;
import com.yanzu.module.member.service.iot.platform.IotPushDataReqVO;
import com.yanzu.module.member.service.iot.platform.IotRoomListRespVO;
import com.yanzu.module.member.service.order.AppOrderService;
import com.yanzu.module.member.service.user.AppUserService;
import com.yanzu.module.member.service.wx.WorkWxService;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

@Slf4j
@Component
public class IotService {

    @Value("${iot.clientId}")
    private String clientId;
    @Value("${iot.secret}")
    private String secret;

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
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private WorkWxService workWxService;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    @Lazy
    private AppOrderService appOrderService;

    @Resource
    @Lazy
    private AppUserService appUserService;

    @Resource
    @Lazy
    private DeviceService deviceService;

    public void pushData(IotPushDataReqVO iotPushDataReqVO) {
        JSONObject data = iotPushDataReqVO.getData();
        if (ObjectUtils.isEmpty(data)) {
            data = new JSONObject();
        }
        data.put("clientId", clientId);
        iotPushDataReqVO.setData(data);
        IotResult<String> result = iotClient.pushData(iotPushDataReqVO, clientId, secret);

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
        IotDeviceAddBlacklistReqVO reqVO = new IotDeviceAddBlacklistReqVO().setStoreId(storeId).setPhotoUrl(photoUrl).setRemark(remark);
        IotResult<String> resp = iotDeviceClient.addBlacklist(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return resp.getData();
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }

    }

    public void delUserFace(Long storeId, String admitGuid) {
        IotDeviceDelBlacklistReqVO reqVO = new IotDeviceDelBlacklistReqVO().setStoreId(storeId).setAdmitGuid(admitGuid);
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
     * @param response
     */
    public JSONObject iotPlatform(JSONObject json, HttpServletResponse response) {
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
                        callBackFace(data);
                        break;
                    case "call":
                        //客户呼叫
                        callTask(data);
                        break;
                    case "getRoomList":
                        //获取房间列表
                        return getRoomList(data);
                    case "sendBooking":
                        // 用户发起预定
                        return sendBooking(data);
                    case "syncBookingResult":
                        // 预订结果同步
                        return syncBookingResult(data);
                    case "syncBookingStart":
                        //通知三方核销 （开门）
                        return syncBookingStart(data);
                    case "updateBookingResult":
                        // 核销状态同步
                        return updateBookingResult(data);
                    case "getBookingStatus":
                        // 订单状态查询
                        return getBookingStatus(data);
                    case "refundBooking":
                        // 用户申请退款（需业务系统审核）
                        return refundBooking(data);
                    case "cancelBookingResult":
                        // 用户取消预订结果通知
                        return cancelBookingResult(data);
                }
            } else {
                log.error("签名不匹配,{}", sign);
                throw exception(IOT_SIGN_ERROR);
            }
        }
        return null;
    }

    /**
     * 用户取消预订结果通知
     *
     * @param data
     * @return
     */
    private JSONObject cancelBookingResult(JSONObject data) {
        //这里已经收到了取消的结果  可能是商家同意了取消   也可能是用户那边强制让平台取消
        if (!data.containsKey("orderId") || ObjectUtils.isEmpty(data.getString("orderId"))) {
            throw exception(IOT_PARAMS_ERROR);
        }
        OrderInfoDO orderInfoDO = orderInfoMapper.getByOrderNo(data.getString("orderId"));
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            //因为要新增设备操作记录 所以要模拟租户

            //被取消的订单已开始了  那就触发一下关门
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0) {
                deviceService.closeRoomDoor(null, orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), 4);
            }
            //设置订单状态为取消
            orderInfoDO.setStatus(AppEnum.order_status.CANCEL.getValue());
            orderInfoMapper.updateById(orderInfoDO);
            appOrderService.flushRoomStatus(orderInfoDO.getRoomId());
            workWxService.sendYDOrderCancelMsg(orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), orderInfoDO.getOrderNo());
            return null;
        } else {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    /**
     * 用户申请退款（需业务系统审核）
     *
     * @param data
     * @return
     */
    @TenantIgnore//可以忽略租户
    private JSONObject refundBooking(JSONObject data) {
        //这里主要是做通知  发消息提醒
        if (!data.containsKey("orderId") || ObjectUtils.isEmpty(data.getString("orderId"))) {
            throw exception(IOT_PARAMS_ERROR);
        }
        OrderInfoDO orderInfoDO = orderInfoMapper.getByOrderNo(data.getString("orderId"));
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            String reason = data.getString("reason");
            workWxService.sendYDOrderCancelAuthMsg(orderInfoDO.getStoreId(), orderInfoDO.getRoomId(), orderInfoDO.getOrderNo(),reason);
            return null;
        } else {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    /**
     * 订单状态查询
     *
     * @param data
     * @return
     */
    @TenantIgnore//可以忽略租户
    private JSONObject getBookingStatus(JSONObject data) {
        if (!data.containsKey("orderId") || ObjectUtils.isEmpty(data.getString("orderId"))) {
            throw exception(IOT_PARAMS_ERROR);
        }
        JSONObject result = new JSONObject();
        result.put("orderId", data.getString("orderId"));
        result.put("status", 1);//默认不允许退款
        OrderInfoDO orderInfoDO = orderInfoMapper.getByOrderNo(data.getString("orderId"));
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            //未开始或者被取消了(管理员取消或者用户自己小程序点错了取消）允许退款     否则禁止退款
            if (orderInfoDO.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0
                    || orderInfoDO.getStatus().compareTo(AppEnum.order_status.CANCEL.getValue()) == 0) {
                result.put("status", 1);//允许退款
            } else {
                result.put("status", 2);//不允许退款
            }
        }
        return result;
    }

    /**
     * 用户核销 （开门）
     * @param data
     * @return
     */
    private JSONObject syncBookingStart(JSONObject data) {
        if (!data.containsKey("orderId") || ObjectUtils.isEmpty(data.getString("orderId"))) {
            throw exception(IOT_PARAMS_ERROR);
        }
        OrderInfoDO orderInfoDO = orderInfoMapper.getByOrderNo(data.getString("orderId"));
        if (!ObjectUtils.isEmpty(orderInfoDO)) {
            //因为要新增设备操作记录 所以要模拟租户
            appOrderService.openStoreDoor(orderInfoDO.getOrderKey());
            appOrderService.openRoomDoor(orderInfoDO.getOrderKey());
            return null;
        } else {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    /**
     * 核销状态同步
     *
     * @param data
     * @return
     */
    @TenantIgnore
    private JSONObject updateBookingResult(JSONObject data) {
        //
        return null;
    }

    /**
     * 预订结果同步
     *
     * @param data
     * @return
     */
    @TenantIgnore
    private JSONObject syncBookingResult(JSONObject data) {
        //主要处理预定失败的情况  把订单给关闭
        if (!data.containsKey("orderId") || ObjectUtils.isEmpty(data.getString("orderId"))) {
            throw exception(IOT_PARAMS_ERROR);
        }
        Integer status = data.getInteger("status");
        //只处理预定失败
        if (status.compareTo(3) == 0) {
            OrderInfoDO orderInfo = orderInfoMapper.getByOrderNo(data.getString("orderId"));
            if (!ObjectUtils.isEmpty(orderInfo) && orderInfo.getStatus().compareTo(AppEnum.order_status.CANCEL.getValue()) == 0) {
                //只能取消未开始 进行中
                if (orderInfo.getStatus().compareTo(AppEnum.order_status.PENDING.getValue()) == 0
                        || orderInfo.getStatus().compareTo(AppEnum.order_status.START.getValue()) == 0
                ) {

                    orderInfoMapper.updateById(new OrderInfoDO().setOrderId(orderInfo.getOrderId()).setStatus(AppEnum.order_status.CANCEL.getValue()));
                    //刷新房间状态
                    appOrderService.flushRoomStatus(orderInfo.getRoomId());
                    //异步发送微信通知
                    workWxService.sendYDOrderCancelMsg(orderInfo.getStoreId(), orderInfo.getRoomId(), orderInfo.getOrderNo());
                }
            }
        }
        //此接口不需要返回数据
        return null;
    }

    /**
     * 开始预定
     *
     * @param data
     * @return
     */
    private JSONObject sendBooking(JSONObject data) {
        if (!data.containsKey("orderId") || ObjectUtils.isEmpty(data.getString("orderId"))) {
            throw exception(IOT_PARAMS_ERROR);
        }
        if (!data.containsKey("phone") || ObjectUtils.isEmpty(data.getString("phone"))) {
            throw exception(IOT_PARAMS_ERROR);
        }
        Long roomId = data.getLong("roomId");
        AppRoomListVO roomInfo = roomInfoMapper.getInfoById(roomId);
        if (ObjectUtils.isEmpty(roomInfo)) {
            throw exception(IOT_PARAMS_ERROR);
        }
        //以下操作需要模拟租户进行 否则会丢失租户ID
        return TenantUtils.execute(roomInfo.getTenantId(), () -> {
            String phone = data.getString("phone").trim();
            //根据手机号查询出用户
            MemberUserDO user = appUserService.getUserByMobile(phone);
            if (ObjectUtils.isEmpty(user)) {
                //用户不存在则自动创建
                user = appUserService.createUserIfAbsent(phone, getClientIP());
            }
            String orderId = data.getString("orderId");
            Date beginTime = data.getDate("beginTime");
            Date endTime = data.getDate("endTime");
            appOrderService.preOrder(orderId, user.getId(), AppEnum.order_pay_type.YUDING.getValue(), roomId, beginTime, endTime, null, null, null, false, false);
            //校验通过后创建订单
            OrderSaveReqVO reqVO = new OrderSaveReqVO();
            reqVO.setUserId(user.getId())
                    .setStartTime(beginTime)
                    .setEndTime(endTime)
                    .setRoomId(roomId)
                    .setOrderNo(orderId)
                    .setPayType(AppEnum.order_pay_type.YUDING.getValue())
                    .setPrice(data.getInteger("amount"))
                    .setNightLong(false);
            appOrderService.save(reqVO);
            JSONObject result = new JSONObject();
            result.put("orderId", orderId);
            result.put("phone", phone);
            result.put("roomId", roomInfo.getRoomId());
            result.put("roomName", roomInfo.getRoomName());
            return result;
        });
    }

    /**
     * 获取房间列表数据
     *
     * @param data
     * @return
     */
    private JSONObject getRoomList(JSONObject data) {
        if (!data.containsKey("storeId") || data.getLong("storeId") == null) {
            return null;
        }
        Long storeId = data.getLong("storeId");
        List<IotRoomListRespVO> iotRoomList = roomInfoMapper.getIotRoomList(storeId);
        JSONObject result = new JSONObject();
        result.put("list", iotRoomList);
        return result;
    }

    private void callBackFace(JSONObject data) {
        //查找出设备
        IotDeviceRoomInfoVO deviceRoomVO = deviceInfoMapper.getDeviceRoomVO(data.getString("deviceSn"));
        //把照片url转成base64编码
        String base64Image = convertImageToBase64(data.getString("photoUrl"));
        FaceRecordDO faceRecordDO = new FaceRecordDO().setStoreId(deviceRoomVO.getStoreId()).setFaceId(data.getString("faceId")).setDeviceSn(data.getString("deviceSn")).setAdmitGuid(data.getString("admitGuid")).setPhotoUrl(data.getString("photoUrl")).setPhotoData(base64Image).setShowTime(new Date(data.getLong("showTime"))).setType(data.getInteger("type"));
        //模拟租户
        TenantUtils.execute(deviceRoomVO.getTenantId(), () -> {
            faceRecordMapper.insert(faceRecordDO);
        });
    }


    /**
     * 顾客呼叫处理
     *
     * @param data
     */
    private void callTask(JSONObject data) {
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
     *
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


    public String getLockPwd(String sn) {
        IotDeviceBaseVO reqVO = new IotDeviceBaseVO().setDeviceSn(sn);
        reqVO.setTs(new Date().getTime());
        IotResult<JSONObject> resp = iotDeviceClient.getLockPwd(reqVO, clientId, secret);
        if (resp.getCode().intValue() == 0) {
            return resp.getData().getString("pwd");
        } else {
            throw exception(DEVICE_IOT_OP_ERROR, resp.getMsg());
        }
    }

    /**
     * 产生订单后、修改房间信息调用 主动推送房间信息至美团
     *
     * @param roomId
     * @return
     */
    public void updateStock(Long roomId) {
        // updateStockReqVos 发送client请求的vo
        AppRoomListVO roomInfo = roomInfoMapper.getInfoById(roomId);
        UpdateStockReqVO updateStockReqVo = new UpdateStockReqVO();
        updateStockReqVo.setRoomId(roomInfo.getRoomId());
        updateStockReqVo.setRoomName(roomInfo.getRoomName());
        updateStockReqVo.setStoreId(roomInfo.getStoreId());
        updateStockReqVo.setStoreName(roomInfo.getStoreName());
        //时间段被占用信息
        List<JSONObject> timePeriods = new ArrayList<>();
        updateStockReqVo.setTimePeriods(timePeriods);
        //如果房间状态是禁用，那么未来的时间都不能预订
        if (roomInfo.getStatus().compareTo(AppEnum.room_status.DISABLE.getValue()) == 0) {
            JSONObject item = new JSONObject();
            item.put("beginTime", new Date().getTime());
            item.put("endTime", new Date().getTime() + 1000 * 60 * 60 * 24 * 365);//加1年  一直禁用
            item.put("beginMinutes", 0);
            item.put("endMinutes", 24 * 60);//一整天都被占用
            timePeriods.add(item);
        } else {
            //查询出该房间所有订单
            List<OrderInfoDO> orderList = orderInfoMapper.getByRoomId(roomId, null);
            if (!CollectionUtils.isEmpty(orderList)) {
                orderList.forEach(x -> {
                    JSONObject item = new JSONObject();
                    item.put("beginTime", x.getStartTime().getTime());
                    item.put("endTime", x.getEndTime().getTime());
                    item.put("beginMinutes", getMinuteByDate(x.getStartTime()));
                    item.put("endMinutes", getMinuteByDate(x.getEndTime()));
                    timePeriods.add(item);
                });
            }
            //处理房间的每日禁用时间
            // 禁用时间段列表，包含禁用开始时间和结束时间 new TimeRange("02:00", "08:00")
            if (!ObjectUtils.isEmpty(roomInfo.getBanTimeStart()) && !ObjectUtils.isEmpty(roomInfo.getBanTimeStart())) {
                LocalTime bstart = LocalTime.parse(roomInfo.getBanTimeStart());
                LocalTime bend = LocalTime.parse(roomInfo.getBanTimeEnd());
                Integer bStar = convertToMinutes(roomInfo.getBanTimeStart());
                Integer bEnd = convertToMinutes(roomInfo.getBanTimeEnd());
                //至少给5天的禁用时间
                Date currentDay = new Date();
                currentDay.setHours(0);
                currentDay.setMinutes(0);
                currentDay.setSeconds(0);
                for (int i = 0; i < 5; i++) {
                    // 兼容处理禁用时间跨越两天的情况
                    if (bstart.getHour() > bend.getHour()) {
                        //跨天了 加两段
                        JSONObject item1 = new JSONObject();
                        item1.put("beginTime", currentDay.getTime() + bStar * 60 * 1000);
                        item1.put("endTime", currentDay.getTime() + 1000 * 60 * 60 * 24);//当日结束时间
                        item1.put("beginMinutes", bStar);
                        item1.put("endMinutes", 60 * 24);//当日结束时间
                        timePeriods.add(item1);

                        JSONObject item2 = new JSONObject();
                        item2.put("beginTime", currentDay.getTime() + 1000 * 60 * 60 * 24);//次日0时开始
                        item2.put("endTime", currentDay.getTime() + bEnd * 60 * 1000 + 1000 * 60 * 60 * 24);//次日结束时间
                        item2.put("beginMinutes", 0);
                        item2.put("endMinutes", bEnd);//次日结束时间
                        timePeriods.add(item2);
                    } else {
                        //没跨天 只加一段
                        JSONObject item = new JSONObject();
                        item.put("beginTime", currentDay.getTime() + bStar * 60 * 1000);
                        item.put("endTime", currentDay.getTime() + bEnd * 60 * 1000);
                        item.put("beginMinutes", bStar);
                        item.put("endMinutes", bEnd);
                        timePeriods.add(item);
                    }
                }
            }
            //追加当前时间5天后的时间全部禁用
            JSONObject item = new JSONObject();
            item.put("beginTime", new Date().getTime() + 1000 * 60 * 60 * 24 * 5);
            item.put("endTime", new Date().getTime() + 1000 * 60 * 60 * 24 * 365);//加1年  一直禁用
            item.put("beginMinutes", 0);
            item.put("endMinutes", 24 * 60);//一整天都被占用
            timePeriods.add(item);
        }
        IotPushDataReqVO iotPushDataReqVO = new IotPushDataReqVO();
        iotPushDataReqVO.setType("updateRoomInfo");//更新房间库存消息类型
        iotPushDataReqVO.setData(JSON.parseObject(JSON.toJSONString(updateStockReqVo), JSONObject.class));
        pushData(iotPushDataReqVO);
    }

    public static Integer convertToMinutes(String timeStr) {
        String[] parts = timeStr.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    public Integer getMinuteByDate(Date date) {
        // 使用Calendar来计算分钟数
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT")); // 使用GMT以确保不受系统时区影响
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        // 计算从午夜开始到指定时间的总分钟数
        return hours * 60 + minutes;
    }

    /**
     * 用户到店核销
     *
     * @param no
     */
    public void bookingFinish(String no) {
        IotPushDataReqVO iotPushDataReqVO = new IotPushDataReqVO();
        iotPushDataReqVO.setType("bookingFinish");//用户到店核销消息类型
        JSONObject data = new JSONObject();
        data.put("orderId", no);
        iotPushDataReqVO.setData(JSON.parseObject(JSON.toJSONString(data), JSONObject.class));
        pushData(iotPushDataReqVO);
    }
}
