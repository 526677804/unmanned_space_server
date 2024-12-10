package com.yanzu.module.member.service.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GroupPayTimeReqVo {

    @Schema(name = "门店id")
    private Long storeId;

    @Schema(name = "团购券编号")
    private String ticketNo;
}
