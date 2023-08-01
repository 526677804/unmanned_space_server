package com.yanzu.module.member.controller.app.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "miniapp - 用户会员分页列表 Resp VO")
@Data
@ToString(callSuper = true)
public class AppMemberPageRespVO {

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    private Long id;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    private String nickname;

    @Schema(description = "用户头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "/infra/file/get/35a12e57-4297-4faa-bf7d-7ed2f211c952")
    private String avatar;

    @Schema(description = "用户手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    private String mobile;

    @Schema(description = "账户余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal balance;

    @Schema(description = "赠送余额")
    private BigDecimal giftBalance;

    @Schema(description = "总订单数")
    private Integer orderCount;

    @Schema(description = "最近下单时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND,timezone = DateUtils.TIME_ZONE_DEFAULT)
    private Date orderTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND,timezone = DateUtils.TIME_ZONE_DEFAULT)
    private Date createTime;

}
