package com.yanzu.module.member.controller.app.store.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "miniapp - 门店列表查询 Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppStoreAdminReqVO extends PageParam {

    @Schema(description = "查询关键字")
    private String name;

    @Schema(description = "城市名称")
    private String cityName;

    @Schema(hidden = true)
    private Long userId;

}
