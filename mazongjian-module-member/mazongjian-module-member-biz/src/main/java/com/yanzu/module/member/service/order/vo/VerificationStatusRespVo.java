package com.yanzu.module.member.service.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VerificationStatusRespVo {

    @Schema(name = "1.未核销 2.已核销 3.未知状态" +
            "注1：如果返回3（未知状态），美团侧会一直轮询直到返回终态（1或2），体检预订业务除外。\n" +
            "注2：体检预订业务发起退款后，轮询72小时仍返回consume_status=3，平台会发起自动退款。")
    private Integer consumeStatus;

    @Schema(name = "订单id")
    private String orderId;

}
