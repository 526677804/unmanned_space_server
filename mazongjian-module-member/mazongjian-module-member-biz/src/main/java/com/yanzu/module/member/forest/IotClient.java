package com.yanzu.module.member.forest;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Var;
import com.yanzu.module.member.service.iot.iotBean.*;

public interface IotClient {
    /**
     * 发起授权
     */

    @Post(url = "https://iot.scyanzu.com/admin-api/system/oauth2/authorize")
    IotResult authorize(@Query IotAuthReqVO reqVO);


    /**
     * 获取token
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/system/oauth2/token")
    IotResult<IotTokenRespVO> getToken(@Query IotTokenReqVO reqVO);


    /**
     * 刷新token
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/system/oauth2/refreshToken")
    IotResult<IotTokenRespVO> getTokenRefush(@Query IotTokenRefushReqVO reqVO);


    /**
     * 设备绑定
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/bind", headers = {"Authorization:Bearer ${token}"})
    IotResult<String> bind(@JSONBody IotDeviceBaseVO reqVO, @Var("token") String token);

    /**
     * 设备解绑
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/unbind", headers = {"Authorization:Bearer ${token}"})
    IotResult<Boolean> unbind(@JSONBody IotDeviceBaseVO reqVO, @Var("token") String token);


    /**
     * 设备控制
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/send", headers = {"Authorization:Bearer ${token}"})
    IotResult<Boolean> control(@JSONBody IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO, @Var("token") String token);

    /**
     * 重置wifi
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/configWifi", headers = {"Authorization:Bearer ${token}"})
    IotResult<Boolean> configWifi(@JSONBody IotDeviceBaseVO<IotDeviceConfigWifiReqVO> reqVO, @Var("token") String token);

    /**
     * 设置门锁自动关锁
     * @param reqVO
     * @param token
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/setLockAutoLock", headers = {"Authorization:Bearer ${token}"})
    IotResult<Boolean> setLockAutoLock(@JSONBody IotDeviceSetAutoLockReqVO reqVO, @Var("token")String token);
}
