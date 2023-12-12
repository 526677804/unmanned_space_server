package com.yanzu.module.member.controller.admin.groupPay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 团购支付信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GroupPayInfoRespVO extends GroupPayInfoBaseVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10843")
    private Long id;

    @Schema(description = "团购券名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String groupName;

    @Schema(description = "团购券编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String groupNo;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "11090")
    private BigDecimal groupPayPrice;

    @Schema(description = "团购券类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer groupPayType;

    @Schema(description = "门店", requiredMode = Schema.RequiredMode.REQUIRED, example = "15964")
    private Long storeId;

    @Schema(description = "门店名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "15964")
    private String storeName;

    @Schema(description = "订单", example = "31180")
    private Long orderId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
