package com.yanzu.module.member.controller.app.store.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanzu.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.yanzu.framework.common.util.date.DateUtils.TIME_ZONE_DEFAULT;

@Schema(description = "miniapp - 门店管理列表 Response VO")
@Data
@ToString(callSuper = true)
public class AppStoreAdminRespVO {

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13809")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String storeName;

    @Schema(description = "缩略图url")
    private String headImg;

    @Schema(description = "纬度")
    private Double lat;

    @Schema(description = "经度")
    private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "门店状态 值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "美团授权链接")
    private String meituanScope;

    @Schema(description = "房间数量")
    private Integer roomNum;

    @Schema(description = "设备总数量")
    private Integer deviceNum;

    @Schema(description = "设备在线数量")
    private Integer deviceOnlineNum;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date createTime;

    @Schema(description = "订单清洁时间")
    private Integer clearTime;
    @Schema(description = "清洁时开放房间")
    private Boolean clearOpen;
    @Schema(description = "二维码照片")
    private String qrCode;
    @Schema(description = "预约按钮照片")
    private String btnImg;
    @Schema(description = "启用工作日价格")
    private Boolean workPrice;

    @Schema(description = "密码锁数据")
    private String lockData;


    @Schema(description = "过期时间")
    @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY, timezone = TIME_ZONE_DEFAULT)
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY)
    private Date expireTime;


}
