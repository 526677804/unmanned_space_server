package com.yanzu.module.member.controller.admin.bannerinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 广告管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class BannerInfoBaseVO {

    @Schema(description = "图片地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotNull(message = "图片地址不能为空")
    private String imgUrl;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "跳转地址/页面路径", example = "https://www.iocoder.cn")
    private String jumpUrl;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "30782")
    @NotNull(message = "排序不能为空")
    private Integer sortId;

    @Schema(description = "广告类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "广告类型不能为空")
    private Byte type;


}
