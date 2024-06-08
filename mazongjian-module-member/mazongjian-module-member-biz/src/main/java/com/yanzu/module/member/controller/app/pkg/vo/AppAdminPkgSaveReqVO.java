package com.yanzu.module.member.controller.app.pkg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AppAdminPkgSaveReqVO {

    @Schema(description = "套餐id  新增不传")
    private Long pkgId;

    @Schema(description = "套餐名称")
    @NotNull(message = "套餐名称不能为空")
    private String pkgName;

    @Schema(description = "抵扣时长")
    @NotNull(message = "抵扣时长不能为空")
    private Integer hours;

    @Schema(description = "门店id")
    @NotNull(message = "门店不能为空")
    private Long storeId;

    @Schema(description = "房间类型 空=不限 1小包 2中包 3大包 4豪包 5商务包")
    private Integer roomType;

    @Schema(description = "可用时间 0-23数字，空表示不限")
    private List<Integer> enableTime;

    @Schema(description = "可用星期 1-7 数字，空表示不限")
    private List<Integer> enableWeek;

    @Schema(description = "节假日可用")
    private Boolean enableHoliday;

    @Schema(description = "销售价格")
    @NotNull(message = "销售价格不能为空")
    private BigDecimal price;

    @Schema(description = "购买后过期时间(天) 0=不过期")
    @NotNull(message = "过期时间不能为空")
    private Integer expireDay;

    @Schema(description = "单用户最大购买数量 0=不限")
    @NotNull(message = "最大购买数量不能为空")
    private Integer maxNum;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "支持余额下单")
    private Boolean balanceBuy;

    @Schema(description = "排序 ")
    private Integer sortId;
}
