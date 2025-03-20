package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppStoreVipConfigListRespVO {

    @Schema(description = "vipId")
    private Long vipId;

    @Schema(description = "门店")
    private Long storeId;

    @Schema(description = "会员名称")
    private String vipName;

    @Schema(description = "等级")
    private Byte vipLevel;

    @Schema(description = "折扣")
    private Byte vipDiscount;

    @Schema(description = "积分门槛")
    private Integer score;


}
