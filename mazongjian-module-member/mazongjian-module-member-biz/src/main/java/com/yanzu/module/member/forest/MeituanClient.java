package com.yanzu.module.member.forest;

import cn.hutool.json.JSONObject;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Post;
import com.yanzu.module.member.service.meituan.vo.*;

public interface MeituanClient {

    //校验券
/*    {
        "code": 200,
            "msg": "验证校验通过",
            "data": {
        "deal_marketprice": 100,
                "payment_detail": [{
            "amount": 1,
                    "amount_type": 10,
                    "payment_detail_id": "4926962768587886220NWL850101"
        }],
        "dealgroup_id": 794479306,
                "biz_type": 0,
                "count": 1,
                "mobile": "176****5045",
                "deal_price": 1,
                "receiptEndDate": 1701359999000,
                "deal_title": "60分钟足部足疗",
                "receipt_code": "5571630793",
                "deal_id": 993507556
    }
    }*/
    @Post(url = "https://openapi.dianping.com/router/tuangou/receipt/prepare", contentType = "application/x-www-form-urlencoded")
    JSONObject prepare(@Body MeituanPrepareReqVO reqVO);

    //使用券
/*            "deal_marketprice": 100,
                    "disclose_mobile_no": 0,
                    "payment_detail": [{
                "amount": 1,
                        "amount_type": 10,
                        "payment_detail_id": "4926962768587886220NWL850101"
            }],
            "dealgroup_id": 794479306,
            "biz_type": 0,
            "mobile": "176****5045",
            "deal_price": 1,
            "receiptEndDate": 1701359999000,
            "deal_title": "60分钟足部足疗",
            "receipt_code": "5571630793",
            "open_shop_uuid": "6eb50f3547e1195d43eb447b8ab62449",
            "deal_id": 993507556,
            "order_id": "169354224332335240080073"*/
    @Post(url = "https://openapi.dianping.com/router/tuangou/receipt/consume", contentType = "application/x-www-form-urlencoded")
    JSONObject consume(@Body MeituanConsumeReqVO reqVO);

    //退还券
    @Post(url = "https://openapi.dianping.com/router/tuangou/receipt/reverseconsume", contentType = "application/x-www-form-urlencoded")
    JSONObject reverseconsume(@Body MeituanReverseconsumeReqVO reqVO);


    //获取token
    @Post(url = "https://openapi.dianping.com/router/oauth/token", contentType = "application/x-www-form-urlencoded")
    JSONObject getToken(@Body MeituanGetTokenReqVO reqVO);

    //刷新token
    @Post(url = "https://openapi.dianping.com/router/oauth/token", contentType = "application/x-www-form-urlencoded")
    JSONObject refreshToken(@Body MeituanRefreshTokenReqVO reqVO);

    //店铺查询
    @Post(url = "https://openapi.dianping.com/router/oauth/session/scope", contentType = "application/x-www-form-urlencoded")
    JSONObject scope(@Body MeituanScopeReqVO reqVO);
}
