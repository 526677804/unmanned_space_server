package com.yanzu.module.member.controller.admin.storeinfo.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yanzu.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yanzu.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 门店管理 Excel 导出 Request VO，参数和 StoreInfoPageReqVO 是一致的")
@Data
public class StoreInfoExportReqVO {

    @Schema(description = "门店名称", example = "赵六")
    private String storeName;

    @Schema(description = "城市名称", example = "赵六")
    private String cityName;

    @Schema(description = "门店环境照片")
    private String storeEnvImg;

    @Schema(description = "门店公告")
    private String notice;

    @Schema(description = "纬度")
     private Double lat;

    @Schema(description = "经度")
     private Double lon;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "门店状态", example = "2")
    private Integer status;

    @Schema(description = "总收入")
    private BigDecimal totalMoney;

    @Schema(description = "已提现")
    private BigDecimal totalWithdrawal;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
