package com.yanzu.module.member.forest;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Var;
import com.yanzu.module.member.controller.app.store.vo.AppAddLockReqVO;
import com.yanzu.module.member.service.iot.device.*;

public interface IotDeviceClient {

    /**
     * 设备绑定
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/bind",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<String> bind(@JSONBody IotDeviceBaseVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);

    /**
     * 设备解绑
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/unbind",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> unbind(@JSONBody IotDeviceBaseVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);


    /**
     * 设备控制
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/send",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> control(@JSONBody IotDeviceBaseVO<IotDeviceContrlReqVO> reqVO, @Var("clientId") String clientId, @Var("secret") String secret);

    /**
     * 重置wifi
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/configWifi",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> configWifi(@JSONBody IotDeviceBaseVO<IotDeviceConfigWifiReqVO> reqVO, @Var("clientId") String clientId, @Var("secret") String secret);

    /**
     * 设置门锁自动关锁
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/setLockAutoLock",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> setLockAutoLock(@JSONBody IotDeviceSetAutoLockReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);

    /**
     * 更新锁数据
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/lockUpdate",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> lockUpdate(@JSONBody IotDeviceLockUpdateReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);
    /**
     * 添加人脸
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/addBlacklist",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<String> addBlacklist(@JSONBody IotDeviceAddBlacklistReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);

    /**
     * 删除人脸
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/delBlacklist",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> delBlacklist(@JSONBody IotDeviceDelBlacklistReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);

    /**
     * 控制空调
     * @param req
     * @param clientId
     * @param secret
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/controlKT",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> controlKT(@JSONBody IotControlKTReqVO req, @Var("clientId")String clientId, @Var("secret") String secret);

    /**
     * 添加智能锁
     * @param req
     * @param clientId
     * @param secret
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/device/addLock",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<IotAddLockRespVO> addLock(@JSONBody AppAddLockReqVO req, @Var("clientId")String clientId,  @Var("secret") String secret);
}
