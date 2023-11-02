package com.yanzu.module.member.controller.admin.groupPay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 团购支付信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GroupPayInfoUpdateReqVO extends GroupPayInfoBaseVO {

}
