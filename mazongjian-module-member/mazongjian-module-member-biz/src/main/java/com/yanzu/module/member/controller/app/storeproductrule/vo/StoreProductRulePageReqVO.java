package com.yanzu.module.member.controller.app.storeproductrule.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 商品规则值(规格)分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreProductRulePageReqVO extends PageParam {

    @Schema(description = "规格名称", example = "赵六")
    private String ruleName;

}
