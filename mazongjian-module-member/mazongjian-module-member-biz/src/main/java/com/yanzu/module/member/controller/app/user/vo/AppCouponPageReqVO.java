package com.yanzu.module.member.controller.app.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.pojo.PageParam;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

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

    @Schema(description = "是否通宵")
    private Boolean nightLong;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = DateUtils.TIME_ZONE_DEFAULT)
    private Date startTime;


    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = DateUtils.FORMAT_ORDER_TIME)
    @JsonFormat(pattern = DateUtils.FORMAT_ORDER_TIME, timezone = DateUtils.TIME_ZONE_DEFAULT)
    private Date endTime;


    @Schema(description = "用户id", hidden = true)
    private Long userId;


}
