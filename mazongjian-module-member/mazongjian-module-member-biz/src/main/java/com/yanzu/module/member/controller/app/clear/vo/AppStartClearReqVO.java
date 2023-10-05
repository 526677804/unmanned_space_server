package com.yanzu.module.member.controller.app.clear.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "miniapp - 完成保洁任务信息 Req VO")
@Data
@ToString(callSuper = true)
public class AppStartClearReqVO {
    @Schema(description = "任务id")
    @NotNull
    private Long id;

    @Schema(description = "纬度")
//    @NotNull(message = "定位信息不能为空")
    private Double lat;

    @Schema(description = "经度")
//    @NotNull(message = "定位信息不能为空")
    private Double lon;

    @Schema(description = "任务图片url 逗号分隔")
    @NotNull(message = "任务图片信息不能为空")
    private String  imgs;

}
