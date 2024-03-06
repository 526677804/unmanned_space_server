package com.yanzu.module.member.service.iot;

import com.yanzu.module.member.forest.IotClient;
import com.yanzu.module.member.service.iot.iotbean.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @PACKAGE_NAME: com.yanzu.iot
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 14:49
 */
@Slf4j
@Component
public class IotService {

    @Resource
    private IotClient iotClient;

    @Value("${wmj.v1.appid}")
    private String v1appid;
    @Value("${wmj.v1.secret}")
    private String v1secret;

    @Value("${wmj.v2.appid}")
    private String v2appid;
    @Value("${wmj.v2.secret}")
    private String v2secret;


    private IotApiBaseReqVO getIotApiBaseReqVO(){
        IotApiBaseReqVO vo = new IotApiBaseReqVO();
        vo.setAppid(v1appid);
        vo.setAppsecret(v1secret);
        return vo;
    }

    private IotApiV2BaseReqVO getIotApiV2BaseReqVO(){
        IotApiV2BaseReqVO vo = new IotApiV2BaseReqVO();
        vo.setApp_id(v2appid);
        vo.setApp_secret(v2secret);
        return vo;
    }

    public boolean regV1(String sn) {
        IotApiBaseReqVO vo = getIotApiBaseReqVO();
        vo.setSn(sn);
        IotApiBaseRespVO respVO = iotClient.regV1(vo);
        log.info("data:{}", respVO);
        return respVO.getState() == 1 && respVO.getState_code() == 1;
    }

    public boolean regV2(String sn) {
        IotApiV2BaseReqVO vo = getIotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        vo.setType(null);
        IotApiV2BaseRespVO respVO = iotClient.regV2(vo);
        log.info("data:{}", respVO);
        return respVO.getCode() == 0;

    }

    public boolean regV2Door(String sn) {
        IotApiV2BaseReqVO vo = getIotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        vo.setType(null);
        IotApiV2RegDoorReqVO data = new IotApiV2RegDoorReqVO();
        data.setData(new IotApiV2RegDoorDataReqVO());
        vo.setData(data);
        IotApiV2BaseRespVO respVO = iotClient.regV2Door(vo);
        log.info("data:{}", respVO);
        return respVO.getCode() == 0;

    }

    public boolean configYunlaba(String sn) {
        IotApiV2BaseReqVO vo = getIotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        YunlabaOpVO<YunlabaConfigReqVO> data = new YunlabaOpVO();
        YunlabaConfigReqVO info = new YunlabaConfigReqVO();
        data.setInfo(info);
        vo.setData(data);
        IotApiV2BaseRespVO respVO = iotClient.configYunlaba(vo);
        log.info("data:{}", respVO);
        return respVO.getCode() == 0;

    }

    public boolean runDoorV1(String sn) {
        IotApiBaseReqVO vo = getIotApiBaseReqVO();
        vo.setSn(sn);
        IotApiBaseRespVO respVO = iotClient.runDoorV1(vo);
        log.info("data:{}", respVO);
        return respVO.getState_code() == 1 && respVO.getState_msg().equals("成功");
    }

    public boolean runDoorV2(String sn) {
        IotApiV2BaseReqVO vo = getIotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        KongkaiOpVO data = new KongkaiOpVO();
        data.setCmd_type("open");
        vo.setData(data);
        IotApiV2BaseRespVO respVO = iotClient.runKongkai(vo);
        log.info("data:{}", respVO);
        return respVO.getCode() == 0;
    }

    public boolean runKongkai(String sn, String cmd) {
        IotApiV2BaseReqVO vo = getIotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        KongkaiOpVO data = new KongkaiOpVO();
        data.setCmd_type(cmd);
        vo.setData(data);
        IotApiV2BaseRespVO respVO = iotClient.runKongkai(vo);
        log.info("data:{}", respVO);
        return respVO.getCode() == 0;
    }

    public boolean runConfigWifi(String sn, String cmd) {
        IotApiV2BaseReqVO vo = getIotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        KongkaiOpVO data = new KongkaiOpVO();
        data.setCmd_type(cmd);
        vo.setData(data);
        IotApiV2BaseRespVO respVO = iotClient.runKongkai(vo);
        log.info("data:{}", respVO);
        return respVO.getCode() == 0;
    }

    public boolean runYunlaba(String sn, String tts) {
        IotApiV2BaseReqVO vo = getIotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        YunlabaOpVO<YunlabaInfoVO> data = new YunlabaOpVO();
        YunlabaInfoVO info = new YunlabaInfoVO();
        info.setTts(tts);
        info.setInner(10);
        data.setInfo(info);
        vo.setData(data);
        IotApiV2BaseRespVO respVO = iotClient.runYunlaba(vo);
        log.info("data:{}", respVO);
        return respVO.getCode() == 0;
    }

    public Integer getV1Status(String sn) {
        IotApiBaseReqVO vo = getIotApiBaseReqVO();
        vo.setSn(sn);
        IotApiOnlineDataVO v1Status = iotClient.getV1Status(vo);
        log.info("data:{}", v1Status);
        if (v1Status.getState() == 1 && v1Status.getState_code() == 1) {
            return v1Status.getOnline();
        }
        return -1;

    }


    public Integer getV2Status(String sn) {
        IotApiV2BaseReqVO vo = getIotApiV2BaseReqVO();
        IotApiV2BaseRespVO<IotApiV2OnlineDataVO> v2Status = iotClient.getV2Status(vo);
        log.info("data:{}", v2Status);
        if (v2Status.getCode() == 0) {
            return v2Status.getData().getOn_line();
        }
        return -1;
    }

}
