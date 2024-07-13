package com.yanzu.module.member.service.iot;

import com.yanzu.module.member.forest.IotGroupPayClient;
import com.yanzu.module.member.service.iot.device.IotResult;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayConsumeReqVO;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareReqVO;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareRespVO;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayScopeUrlReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.IOT_ERROR;

@Slf4j
@Component
public class IotGroupPayService {

    @Value("${iot.clientId}")
    private String clientId;
    @Value("${iot.secret}")
    private String secret;
    @Value("${iot.redirectUrl}")
    private String redirectUrl;

    @Resource
    private IotGroupPayClient iotGroupPayClient;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取团购平台授权URL地址
     *
     * @param storeId      系统内的门店id
     * @param groupPayType 1=美团 2=抖音 3=快手
     * @return
     */
    public String getScopeUrl(Long storeId, Integer groupPayType) {
        IotGroupPayScopeUrlReqVO reqVO = new IotGroupPayScopeUrlReqVO();
        reqVO.setStoreId(storeId).setGroupPayType(groupPayType);
        IotResult<String> result = iotGroupPayClient.getScopeUrl(reqVO, clientId, secret);
        if (result.getCode().intValue() == 0) {
            return result.getData();
        } else {
            throw exception(IOT_ERROR, result.getMsg());
        }
    }

    /**
     * 验券准备
     * @param reqVO
     * @return
     */
    public IotGroupPayPrepareRespVO prepare(IotGroupPayPrepareReqVO reqVO){
        IotResult<IotGroupPayPrepareRespVO> result = iotGroupPayClient.prepare(reqVO, clientId, secret);
        if (result.getCode().intValue() == 0) {
            return result.getData();
        } else {
            throw exception(IOT_ERROR, result.getMsg());
        }
    }

    /**
     *  使用团购券
     * @param reqVO
     * @return
     */
   public Boolean consume(IotGroupPayConsumeReqVO reqVO){
       IotResult<Boolean> result = iotGroupPayClient.consume(reqVO, clientId, secret);
       if (result.getCode().intValue() == 0) {
           return result.getData();
       } else {
           throw exception(IOT_ERROR, result.getMsg());
       }
   }

    /**
     *  撤销验券
     * @param reqVO
     * @return
     */
    public Boolean revoke(IotGroupPayConsumeReqVO reqVO){
        IotResult<Boolean> result = iotGroupPayClient.revoke(reqVO, clientId, secret);
        if (result.getCode().intValue() == 0) {
            return result.getData();
        } else {
            throw exception(IOT_ERROR, result.getMsg());
        }
    }


}
