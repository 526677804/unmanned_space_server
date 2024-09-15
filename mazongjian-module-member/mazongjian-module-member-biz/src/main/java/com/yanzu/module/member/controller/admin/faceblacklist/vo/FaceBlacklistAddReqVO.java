package com.yanzu.module.member.controller.admin.faceblacklist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FaceBlacklistAddReqVO {

    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    @Schema(description = "备注")
    private String remark;
}
