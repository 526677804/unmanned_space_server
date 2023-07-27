package com.yanzu.module.member.controller.app.manager.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "miniapp - 赠送优惠券列表 Request VO")
@Data
@ToString(callSuper = true)
public class AppPresentCouponPageReqVO extends PageParam {

    @Schema(description = "状态 值见字典")
    private Integer status;

    @Schema(description = "门店Id")
    private Long storeId;
}
