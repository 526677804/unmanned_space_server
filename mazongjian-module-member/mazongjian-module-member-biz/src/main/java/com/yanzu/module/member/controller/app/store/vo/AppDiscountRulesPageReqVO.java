package com.yanzu.module.member.controller.app.store.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "miniapp - 获取门店充值优惠规则分页 Req VO")
@Data
@ToString(callSuper = true)
public class AppDiscountRulesPageReqVO extends PageParam {

    @Schema(description = "门店Id")
    private Long storeId;


    @Schema(hidden = true)
    private Long userId;

}
