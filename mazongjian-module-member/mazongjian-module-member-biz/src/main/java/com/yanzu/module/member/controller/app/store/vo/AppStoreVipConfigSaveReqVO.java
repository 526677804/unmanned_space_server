package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AppStoreVipConfigSaveReqVO {

    @Schema(description = "门店")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "会员名称")
    @NotNull(message = "会员名称不能为空")
    @Length(max = 3, min = 1, message = "会员名称最多3个字")
    private String vipName;

    @Schema(description = "折扣")
    @NotNull(message = "折扣不能为空")
    @Max(value = 99,message = "折扣最大99")
    @Min(value = 1,message = "折扣最小1")
    private Byte vipDiscount;

    @Schema(description = "积分门槛")
    @NotNull(message = "积分门槛不能为空")
    @Max(value = 999999,message = "积分门槛最大99999")
    @Min(value = 1,message = "积分门槛最小1")
    private Integer score;


}
