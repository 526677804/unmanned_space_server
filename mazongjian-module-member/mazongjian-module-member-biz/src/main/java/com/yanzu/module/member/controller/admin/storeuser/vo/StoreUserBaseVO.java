package com.yanzu.module.member.controller.admin.storeuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 门店用户管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class StoreUserBaseVO {

    @Schema(description = "门店id", requiredMode = Schema.RequiredMode.REQUIRED, example = "21367")
    @NotNull(message = "门店id不能为空")
    private Long storeId;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10508")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "真实姓名", example = "赵六")
    private String name;

}
