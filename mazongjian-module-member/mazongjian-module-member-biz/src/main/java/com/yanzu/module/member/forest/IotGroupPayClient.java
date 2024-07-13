package com.yanzu.module.member.forest;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Var;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import com.yanzu.module.member.service.iot.device.IotResult;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayConsumeReqVO;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareReqVO;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareRespVO;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayScopeUrlReqVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.security.PermitAll;

public interface IotGroupPayClient {

    /**
     * 获取团购平台授权URL地址
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/groupPay/getScopeUrl",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<String> getScopeUrl(@JSONBody IotGroupPayScopeUrlReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);



    /**
     * 验券准备
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/groupPay/prepare",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<IotGroupPayPrepareRespVO> prepare(@JSONBody IotGroupPayPrepareReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);

    /**
     * 使用团购券
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/groupPay/consume",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> consume(@JSONBody IotGroupPayConsumeReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);


    /**
     * 撤销验券
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/groupPay/revoke",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> revoke(@JSONBody IotGroupPayConsumeReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);


}
