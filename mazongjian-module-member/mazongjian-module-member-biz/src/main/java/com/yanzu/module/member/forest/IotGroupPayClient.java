package com.yanzu.module.member.forest;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Var;
import com.yanzu.module.member.service.iot.device.IotResult;
import com.yanzu.module.member.service.iot.groupPay.*;

import java.util.List;

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

    /**
     * 手机号查询可用团购券
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/groupPay/queryCodeByMobile",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<List<IotGroupPaySelectByPhoneRespVO>> selectGroupPayByPhone(@JSONBody IotGroupPaySelectByPhoneReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);


    /**
     * 获取预订退款待审核列表
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/groupPay/getYDCancelAuthList",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<List<IotGroupPayGetYDCancelAuthListRespVO>> getYDCancelAuthList(@JSONBody IotGroupPayGetYDCancelAuthListReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);

    /**
     * 审核预订取消
     *
     * @param reqVO
     * @return
     */
    @Post(url = "https://iot.scyanzu.com/admin-api/iot/groupPay/auditYD",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<Boolean> auditYD(@JSONBody IotGroupPayAuditYDReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);


}
