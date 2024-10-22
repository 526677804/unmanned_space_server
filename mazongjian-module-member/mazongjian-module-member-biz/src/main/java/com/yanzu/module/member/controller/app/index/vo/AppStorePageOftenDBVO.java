package com.yanzu.module.member.controller.app.index.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.index.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 17:01
 */
@Schema(description = "miniapp - 首页 获取门店列表 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppStorePageOftenDBVO extends PageParam {

    @Schema(description = "城市名称")
    private String cityName;

    @Schema(description = "名称门店")
    private String name;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "附近false , 常用true")
    private String often;

    @Schema(description = "常用门店ids")
    private List<Long> storeIds;

}
