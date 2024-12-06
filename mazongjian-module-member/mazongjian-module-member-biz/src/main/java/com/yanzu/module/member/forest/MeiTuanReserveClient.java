package com.yanzu.module.member.forest;

import com.dtflys.forest.annotation.*;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.module.member.controller.app.meituanreserve.vo.MeituanYudingBookResultCallbackReqVo;
import com.yanzu.module.member.controller.app.meituanreserve.vo.StoreRulePushReqVo;
import com.yanzu.module.member.controller.app.meituanreserve.vo.UpdateStockReqVo;

import java.util.List;

/**
 * 发送请求至ws
 */
public interface MeiTuanReserveClient {

    /**
     * 主动推送规则  todo 成功
     * @param reqVO
     * @param clientId
     * @param secret
     * @return
     */
    @Post(url = "https://iot-test.scyanzu.com/admin-api/meituan/yuding/ddzhYudingUpdatebookrule",contentType = "application/json",headers = {
            "clientId:${clientId}",
            "secret:${secret}",
    })
    CommonResult pushRule(@Body StoreRulePushReqVo reqVO, @Var("clientId") String clientId, @Var("secret") String secret);


    /**
     * 产生订单后、修改房间信息调用 主动推送房间信息至美团  todo 未测试
     * @param reqVO
     * @param clientId
     * @param secret
     * @return
     */
    @Post(url = "https://iot-test.scyanzu.com/admin-api/meituan/yuding/ddzhYudingUpdatestock",contentType = "application/json",headers = {
            "clientId:${clientId}",
            "secret:${secret}",
    })
    CommonResult pushData(@Body UpdateStockReqVo reqVO, @Var("clientId") String clientId, @Var("secret") String secret);


    /**
     * 开始预定之后执行  将预定结果推送美团   todo 未测试
     * @param reqVo
     * @param clientId
     * @param secret
     * @return
     */
    @Post(url = "https://iot-test.scyanzu.com/admin-api/meituan/yuding/bookResultCallback",contentType = "application/json",headers = {
            "clientId:${clientId}",
            "secret:${secret}",
    })
    CommonResult reserveResult(@Body MeituanYudingBookResultCallbackReqVo reqVo, @Var("clientId") String clientId, @Var("secret") String secret);

}
