package com.yanzu.module.member.controller.app.meituanreserve.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MeiTuanReserveRespVo {

    //消息类型
    @Schema(description = "消息类型")
    private Integer msgType;
    //消息推送时间戳（秒）
    @Schema(description = "消息推送时间戳（秒）")
    private Long timestamp;
    //消息的安全签名，可以根据此签名判断消息真伪。开发者需要对请求消息的签名进行验证，签名规则见：签名规则
//    @Schema(description = "安全签名", required = true)
//    @NotBlank(message = "安全签名不能为空")
//    private String sign;
//    //开发者id
//    @Schema(description = "开发者id", required = true)
//    @NotNull(message = "开发者id不能为空")
//    private Integer developerId;

    //开发者id
    @Schema(description = "开发者id")
    private String clientId;

    //消息所属的业务id
//    @Schema(description = "业务id", required = true)
//    @NotNull(message = "业务id不能为空")
//    private Integer businessId;
    //本条消息的唯一标识，可用于去重判断
    @Schema(description = "消息的唯一标识")
    private String msgId;
    //业务实体id 可为空
//    @Schema(description = "业务实体id")
//    private String opBizCode;
    //服务商门店id（外卖/团购等业务适用）可为空
//    @Schema(description = "服务商门店id", required = true)
//    @NotBlank(message = "服务商门店id不能为空")
//    private String ePoiId;

    //服务商门店id（外卖/团购等业务适用）可为空
    @Schema(description = "店铺id")
    private Long storeId;
    //json格式的消息数据
    @Schema(description = "json格式的消息数据")
    private String message;

}
