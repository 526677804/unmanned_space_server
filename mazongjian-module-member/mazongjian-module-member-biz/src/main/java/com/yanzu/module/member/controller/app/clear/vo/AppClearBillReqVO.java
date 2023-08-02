package com.yanzu.module.member.controller.app.clear.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "miniapp - 保洁账单信息的  Req VO")
@Data
@ToString(callSuper = true)
public class AppClearBillReqVO extends PageParam {

    @Schema(hidden = true)
    private Long userId;

}
