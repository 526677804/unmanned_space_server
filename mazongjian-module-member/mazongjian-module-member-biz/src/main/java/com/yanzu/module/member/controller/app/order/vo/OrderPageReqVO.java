package com.yanzu.module.member.controller.app.order.vo;

import com.yanzu.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.index.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 14:47
 */
@Schema(description = "miniapp - 订单分页列表Req VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPageReqVO extends PageParam {

    @Schema(description = "状态 0未开始 1进行中 2已完成 3已取消")
    private Integer status;

    @Schema(description = "排序字段 传startTime 或 createTime")
    private String orderColumn;

    @Schema(description = "门店Id")
    private Long storeId;

    @Schema(hidden = true)
    private Long userId;

    @Schema(hidden = true)
    private String storeIds;


}
