package com.yanzu.module.member.controller.app.manager.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.manager.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/27 9:50
 */
@Schema(description = "miniapp - 管理员获取保洁员用户列表 Resp VO")
@Data
@ToString(callSuper = true)
public class AppClearUserPageRespVO {

    @Schema(description = "id 数据的id,不是userId")
    private Long id;

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "门店Id")
    private Long storeId;

    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    private String nickname;

    @Schema(description = "用户头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "/infra/file/get/35a12e57-4297-4faa-bf7d-7ed2f211c952")
    private String avatar;

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    private String name;

    @Schema(description = "用户手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    private String mobile;

    @Schema(description = "状态 值见字典", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Schema(description = "已完成", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer finishCount;

    @Schema(description = "已结算", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer settlementCount;

    @Schema(description = "收入", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal money;

}
