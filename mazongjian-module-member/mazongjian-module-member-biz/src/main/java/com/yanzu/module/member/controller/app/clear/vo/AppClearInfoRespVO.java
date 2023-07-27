package com.yanzu.module.member.controller.app.clear.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Schema(description = "miniapp - 保洁信息的  Response VO")
@Data
@ToString(callSuper = true)
public class AppClearInfoRespVO {

    @Schema(description = "清洁记录id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13416")
    private Long clearId;

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "30333")
    private Long orderId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderNo;

    @Schema(description = "门店Id")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "房间名称")
    private String roomName;

    @Schema(description = "房间类型")
    private Integer roomType;

    @Schema(description = "用户id", example = "23617")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "订单结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date orderEndTime;

    @Schema(description = "清洁图片url")
    private String imgs;

    @Schema(description = "驳回的照片url")
    private String complaintImgs;

    @Schema(description = "驳回的原因")
    private String complaintDesc;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "完成时间")
    private Date finishTime;

    @Schema(description = "结算时间")
    private Date settlementTime;

    @Schema(description = "状态 值见字典", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String status;

    @Schema(description = "创建时间/接单时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;





}
