package com.yanzu.module.member.controller.app.manager.vo;

import com.yanzu.framework.common.pojo.PageParam;
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
@Schema(description = "miniapp - 管理员获取保洁员用户列表 Req VO")
@Data
@ToString(callSuper = true)
public class AppClearUserPageReqVO extends PageParam {

    @Schema(description = "门店id")
    private Long storeId;


}
