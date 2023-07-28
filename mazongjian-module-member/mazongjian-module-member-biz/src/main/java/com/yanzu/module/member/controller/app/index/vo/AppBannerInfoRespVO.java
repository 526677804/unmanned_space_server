package com.yanzu.module.member.controller.app.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.index.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/28 9:57
 */
@Schema(description = "miniapp - 广告列表 Resp VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppBannerInfoRespVO {
    @Schema(description = "图片地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    private String imgUrl;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "跳转地址/页面路径", example = "https://www.iocoder.cn")
    private String jumpUrl;
}
