package com.yanzu.module.member.controller.admin.discountrules.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 充值优惠规则管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DiscountRulesCreateReqVO extends DiscountRulesBaseVO {

}
