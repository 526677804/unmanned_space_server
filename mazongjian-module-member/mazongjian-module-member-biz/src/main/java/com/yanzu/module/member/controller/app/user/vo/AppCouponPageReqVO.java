package com.yanzu.module.member.controller.app.user.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.user.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 18:10
 */
@Schema(description = "miniapp - 用户优惠券列表 Request VO")
@Data
@ToString(callSuper = true)
public class AppCouponPageReqVO extends PageParam {

    @Schema(description = "状态 值见字典")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "房间类型 值见字典")
    private Integer roomType;

    @Schema(description = "门店Id")
    private Long storeId;

}
