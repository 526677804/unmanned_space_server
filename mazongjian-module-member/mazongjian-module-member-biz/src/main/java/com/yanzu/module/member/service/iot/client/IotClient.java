package com.yanzu.module.member.service.iot.client;

import com.dtflys.forest.annotation.*;
import com.yanzu.module.member.service.iot.bean.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public interface IotClient {

    /**
     * 注册V1 设备
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://www.wmj.com.cn/api/reglock")
    IotApiBaseRespVO regV1(@Body IotApiBaseReqVO reqVO);

    /**
     * 注册V2 设备
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://wdev.wmj.com.cn/deviceApi/register")
    IotApiV2BaseRespVO regV2(@JSONBody IotApiV2BaseReqVO reqVO);


    /**
     * 调用门禁
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://www.wmj.com.cn/api/oplock")
    IotApiBaseRespVO runDoorV1(@Body IotApiBaseReqVO reqVO);

    /**
     * 调用空开
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://wdev.wmj.com.cn/deviceApi/mqtt/send")
    IotApiV2BaseRespVO<IotApiV2DataVO> runKongkai(@JSONBody IotApiV2BaseReqVO<KongkaiOpVO> reqVO);


    /**
     * 调用云喇叭
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://wdev.wmj.com.cn/deviceApi/mqtt/send")
    IotApiV2BaseRespVO<IotApiV2DataVO> runYunlaba(@JSONBody IotApiV2BaseReqVO<YunlabaOpVO> reqVO);

    /**
     * 查询v2设备的状态
     */
    @Post(url = "https://wdev.wmj.com.cn/deviceApi/getOnLine")
    IotApiV2BaseRespVO<IotApiV2OnlineDataVO> getV2Status(@JSONBody IotApiV2BaseReqVO reqVO);
    /**
     * 查询v1设备的状态
     */
    @Post(url = "https://www.wmj.com.cn/api/lockstatus")
    IotApiOnlineDataVO getV1Status(@Body IotApiBaseReqVO reqVO);


}
