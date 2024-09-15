package com.yanzu.module.member.forest;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Query;
import com.dtflys.forest.annotation.Var;
import com.yanzu.module.member.service.iot.device.IotAuthReqVO;
import com.yanzu.module.member.service.iot.device.IotDeviceBaseVO;
import com.yanzu.module.member.service.iot.device.IotResult;
import com.yanzu.module.member.service.iot.platform.IotPushDataReqVO;

public interface IotClient {

    /**
     * 应用上线
     */

    @Post(url = "https://iot.scyanzu.com/admin-api/iot/platform/pushData",
            headers = {
                    "clientId:${clientId}",
                    "secret:${secret}",
            })
    IotResult<JSONBody> pushData(@JSONBody IotPushDataReqVO reqVO, @Var("clientId") String clientId, @Var("secret") String secret);



}
