package com.yanzu.module.member.controller.admin.storeinfo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Schema(description = "管理后台 - 门店管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StoreInfoUpdateReqVO extends StoreInfoBaseVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2995")
    @NotNull(message = "门店ID不能为空")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotNull(message = "门店名称不能为空")
    private String storeName;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotNull(message = "城市名称不能为空")
    private String cityName;

    @Schema(description = "门店介绍")
    private String content;

    @Schema(description = "门店公告")
    private String notice;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "门店状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "门店状态不能为空")
    private Integer status;

    @Schema(description = "wifi信息")
    private String wifiInfo;

    @Schema(description = "wifi密码")
    private String wifiPwd;

    @Schema(description = "客服电话")
    private String kefuPhone;

    @Schema(description = "订单通知webhook")
    private String orderWebhook;

    @Schema(description = "美团店铺uuid")
    private String meituanOpenShopUuid;

    @Schema(description = "抖音poiId")
    private String douyinPoiId;


}
