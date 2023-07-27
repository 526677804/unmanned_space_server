package com.yanzu.module.member.controller.admin.clearbill.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 保洁账单管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClearBillCreateReqVO extends ClearBillBaseVO {

}
