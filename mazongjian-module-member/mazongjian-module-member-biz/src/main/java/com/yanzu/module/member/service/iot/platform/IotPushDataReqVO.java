package com.yanzu.module.member.service.iot.platform;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IotPushDataReqVO {

    //类型 online=上线
    @Schema(description = "类型")
    @NotNull(message = "type不能为空")
    private String type;

    //数据
    private JSONObject data;

}
