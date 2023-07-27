package com.yanzu.module.member.controller.admin.usermoneybill.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 用户账单明细创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserMoneyBillCreateReqVO extends UserMoneyBillBaseVO {

}
