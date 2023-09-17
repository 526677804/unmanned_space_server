package com.yanzu.module.member.controller.app.user.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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

    @Schema(description = "房间id(下单的时候传)")
    private Long roomId;

    @Schema(description = "时长/小时0.5为单位(下单的时候传)")
    private BigDecimal orderHour;

    @Schema(description = "用户id", hidden = true)
    private Long userId;

}
