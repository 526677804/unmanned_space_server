package com.yanzu.module.member.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.user.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 16:21
 */
@Schema(description = "miniapp - 用户赠送余额分页 Resp VO")
@Data
@ToString(callSuper = true)
public class AppGiftBalanceListRespVO {
    @Schema(description = "门店名称", example = "总店")
    private String storeName;

    @Schema(description = "余额", example = "2319.01")
    private BigDecimal balance;

    @Schema(description = "赠送余额", example = "2319.01")
    private BigDecimal giftBalance;


}
