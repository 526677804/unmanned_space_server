package com.yanzu.module.member.controller.admin.storeinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 门店管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreInfoUpdateReqVO extends StoreInfoBaseVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13809")
    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotNull(message = "门店名称不能为空")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotNull(message = "城市名称不能为空")
    private String cityName;

    @Schema(description = "门店富文本详情")
    private String content;

    @Schema(description = "门店公告")
    private String notice;

    @Schema(description = "纬度")
     private Double lat;

    @Schema(description = "经度")
     private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "门店状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "门店状态不能为空")
    private Integer status;

    @Schema(description = "wifi信息")
    private String wifiInfo;

    @Schema(description = "工作日折扣", example = "25916")
    private Integer workDiscount;

    @Schema(description = "大门门禁sn")
    private String gateDeviceSn;

    @Schema(description = "大众点评key")
    private String dianpinKey;

    @Schema(description = "美团key")
    private String meituanKey;

    @Schema(description = "抖音key")
    private String douyinKey;

}
