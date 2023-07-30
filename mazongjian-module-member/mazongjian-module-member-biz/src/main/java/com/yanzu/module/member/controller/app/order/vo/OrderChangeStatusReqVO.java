package com.yanzu.module.member.controller.app.order.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.index.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 14:47
 */
@Schema(description = "miniapp - 订单修改状态Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderChangeStatusReqVO  {

    @Schema(description = "状态 0未开始 1进行中 3已取消", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "状态不能为空")
    private Long status;


    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单不能为空")
    private Long orderId;

}
