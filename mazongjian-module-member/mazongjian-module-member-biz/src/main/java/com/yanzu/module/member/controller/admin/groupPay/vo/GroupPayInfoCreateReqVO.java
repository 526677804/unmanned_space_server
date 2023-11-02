package com.yanzu.module.member.controller.admin.groupPay.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 团购支付信息创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GroupPayInfoCreateReqVO extends GroupPayInfoBaseVO {

}
