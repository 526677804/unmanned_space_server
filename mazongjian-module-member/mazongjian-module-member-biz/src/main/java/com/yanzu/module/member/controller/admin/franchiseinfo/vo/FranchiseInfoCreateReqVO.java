package com.yanzu.module.member.controller.admin.franchiseinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 加盟信息创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FranchiseInfoCreateReqVO extends FranchiseInfoBaseVO {

}
