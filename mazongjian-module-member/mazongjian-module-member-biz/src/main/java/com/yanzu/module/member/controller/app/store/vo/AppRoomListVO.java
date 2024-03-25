package com.yanzu.module.member.controller.app.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.store.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2024/3/25 12:27
 */
@Data
public class AppRoomListVO {

    @Schema(description = "房间id")
    private Long roomId;

    @Schema(description = "门店id")
    private Long storeId;

    @Schema(description = "房间名称")
    private String roomName;

    @Schema(description = "门店名称")
    private String storeName;


    @Schema(description = "webhook地址")
    private String orderWebhook;




}
