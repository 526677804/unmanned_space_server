package com.yanzu.module.member.controller.app.meituanreserve.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MeituanYudingBookResultCallbackReqVo {

    @Schema(name = "门店id")
    private Long storeId;

    @Schema(name = "美团订单id")
    private String orderId;

    @Schema(name = "2-预定成功,3-预定失败")
    private Integer bookStatus;

    @Schema(name = "业务code，应遵从预订接口的错误码，成功返回200；预订失败时用户侧会根据code展示对应文案，详细可参考参数说明")
    private Integer code;
}
