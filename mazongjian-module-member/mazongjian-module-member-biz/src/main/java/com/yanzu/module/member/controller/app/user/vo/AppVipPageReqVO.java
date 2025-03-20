package com.yanzu.module.member.controller.app.user.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "miniapp - 用户会员分页列表 Request VO")
@Data
@ToString(callSuper = true)
public class AppVipPageReqVO extends PageParam {

    @Schema(description = "查询关键字")
    private String name;

    @Schema(description = "门店ID")
    private Long storeId;

}
