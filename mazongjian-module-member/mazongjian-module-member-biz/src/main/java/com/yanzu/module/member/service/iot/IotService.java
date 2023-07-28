package com.yanzu.module.member.service.iot;

import com.yanzu.module.member.service.iot.bean.*;
import com.yanzu.module.member.service.iot.client.IotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @PACKAGE_NAME: com.yanzu.iot
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/17 14:49
 */
@Component
public class IotService {

    @Resource
    private IotClient iotClient;

    public boolean regV1(String sn) {
        IotApiBaseReqVO vo = new IotApiBaseReqVO();
        vo.setSn(sn);
        IotApiBaseRespVO respVO = iotClient.regV1(vo);
        return respVO.getState() == 1 && respVO.getState_code() == 1;
    }

    public boolean regV2(String sn) {
        IotApiV2BaseReqVO vo = new IotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        vo.setType(null);
        IotApiV2BaseRespVO respVO = iotClient.regV2(vo);
        return respVO.getCode() == 0;

    }

    public boolean runDoorV1(String sn) {
        IotApiBaseReqVO vo=new IotApiBaseReqVO();
        vo.setSn(sn);
        IotApiBaseRespVO respVO = iotClient.runDoorV1(vo);
        return respVO.getState_code() == 0;
    }


    public boolean runKongkai(String sn, String cmd) {
        IotApiV2BaseReqVO vo = new IotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        KongkaiOpVO data = new KongkaiOpVO();
        data.setCmd_type(cmd);
        vo.setData(data);
        IotApiV2BaseRespVO respVO = iotClient.runKongkai(vo);
        return respVO.getCode() == 0;
    }

    public boolean runYunlaba(String sn, String tts) {
        IotApiV2BaseReqVO vo = new IotApiV2BaseReqVO();
        vo.setDevice_sn(sn);
        YunlabaOpVO data = new YunlabaOpVO();
        YunlabaInfoVO info = new YunlabaInfoVO();
        info.setTts(tts);
        info.setInner(10);
        data.setInfo(info);
        vo.setData(data);
        IotApiV2BaseRespVO respVO = iotClient.runYunlaba(vo);
        return respVO.getCode() == 0;
    }

    public Integer getV1Status(String sn) {
        IotApiBaseReqVO vo = new IotApiBaseReqVO();
        vo.setSn(sn);
        IotApiOnlineDataVO v1Status = iotClient.getV1Status(vo);
        if (v1Status.getState() == 1 && v1Status.getState_code() == 1) {
            return v1Status.getOnline();
        }
        return -1;

    }


    public Integer getV2Status(String sn) {
        IotApiV2BaseReqVO vo = new IotApiV2BaseReqVO();
        IotApiV2BaseRespVO<IotApiV2OnlineDataVO> v2Status = iotClient.getV2Status(vo);
        if (v2Status.getCode() == 0) {
            return v2Status.getData().getOn_line();
        }
        return -1;
    }

}
