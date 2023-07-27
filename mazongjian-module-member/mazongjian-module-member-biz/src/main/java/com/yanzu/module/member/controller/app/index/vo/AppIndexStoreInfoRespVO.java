package com.yanzu.module.member.controller.app.index.vo;

import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 门店管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppIndexStoreInfoRespVO extends StoreInfoBaseVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13809")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String cityName;

    @Schema(description = "门店富文本详情")
    private String content;

    @Schema(description = "缩略图url")
    private String headImg;

    @Schema(description = "门店环境/门店风采照片url")
    private String storeEnvImg;

    @Schema(description = "门店公告")
    private String notice;

    @Schema(description = "纬度")
     private Double lat;

    @Schema(description = "经度")
     private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "wifi信息")
    private String wifiInfo;

    @Schema(description = "客服电话")
    private String kefuPhone;

    @Schema(description = "工作日折扣 1-100", example = "8")
    private Integer workDiscount;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
