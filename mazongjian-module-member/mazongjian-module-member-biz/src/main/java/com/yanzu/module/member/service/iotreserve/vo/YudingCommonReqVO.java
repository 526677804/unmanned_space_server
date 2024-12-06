package com.yanzu.module.member.service.iotreserve.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 预订公共请求
 */
@Data
public class YudingCommonReqVO {

    /**
     * 平台类型
     */
    @Schema(description = "平台类型")
    @NotNull
   private Integer platformType;

    /**
     * 预订请求类型：具体某个请求
     */
    @Schema(description = "预订请求类型")
    @NotNull
    private Integer yudingRequestType;

    /**
     * 门店id
     */
    @Schema(description = "门店id")
    @NotNull(message = "门店id不能为空")
    private Long storeId;


    /**
     * 业务数据
     */
    @Schema(description = "业务数据, json格式")
    private String data;

}
