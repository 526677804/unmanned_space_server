package com.yanzu.module.member.controller.app.manager.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "miniapp - 管理优惠券列表 Request VO")
@Data
@ToString(callSuper = true)
public class AppManagerCouponPageReqVO extends PageParam {

    @Schema(description = "优惠券类型 值见字典")
    private Integer type;

    @Schema(description = "门店Id")
    private Long storeId;
}
