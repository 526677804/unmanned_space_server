package com.yanzu.module.member.controller.app.storeproductreply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 评论更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreProductReplyUpdateReqVO extends StoreProductReplyBaseVO {

    @Schema(description = "评论ID", required = true, example = "7419")
    @NotNull(message = "评论ID不能为空")
    private Long id;

}
