package com.yanzu.module.member.controller.app.callback.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * https://developer.meituan.com/docs/biz/biz_xn202005112_12d6af70-ac29-4d12-864c-c154a786e4fd
 * 预订消息公共响应
 */
@Slf4j
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeituanYudingMsgCallbackCommonRespVo {

    //状态码 0为正常，非0为异常
    @Schema(description = "状态码", example = "0为正常，非0为异常")
    private Integer code;
    //消息字段 错误信息or "success"，出错情况下，存放出错信息，用于问题定位。
    @Schema(description = "消息字段", example = "错误信息or \"success\"，出错情况下，存放出错信息，用于问题定位。")
    private String message;
    //在收到消息回调后若需主动返回数据，使用此字段。此字段需要将对象序列化为json string。
    @Schema(description = "业务数据", example = "在收到消息回调后若需主动返回数据，使用此字段。此字段需要将对象序列化为json string。")
    private String data;

    /**
     * 成功返回
     * @param message
     * @param data
     * @return
     */
    public static MeituanYudingMsgCallbackCommonRespVo ok(String message, String data) {
        return MeituanYudingMsgCallbackCommonRespVo.builder().build().setCode(0)
                .setMessage(message)
                .setData(data);
    }

    /**
     * 错误结果
     * @param message
     * @return
     */
    public static MeituanYudingMsgCallbackCommonRespVo error(String message) {
        return MeituanYudingMsgCallbackCommonRespVo.builder().build().setCode(-1)
                .setMessage(message);
    }

    /**
     * 从jsonObject 结果中获取相应结果
     * @param jsonObject
     * @return
     */
    @SneakyThrows
    public static MeituanYudingMsgCallbackCommonRespVo fromJSONObject(JSONObject jsonObject) {
        String jsonString = JSON.toJSONString(jsonObject);
        if(StringUtils.isBlank(jsonString)) {
            return error("系统异常");
        }

        MeituanYudingMsgCallbackCommonRespVo meituanYudingMsgCallbackCommonRespVo = null;
        try {
            meituanYudingMsgCallbackCommonRespVo = JSON.parseObject(jsonString, MeituanYudingMsgCallbackCommonRespVo.class);

        }catch (Exception e) {
            return error("系统异常");
        }
        if(meituanYudingMsgCallbackCommonRespVo == null) {
            return error("系统异常");
        }
        if(meituanYudingMsgCallbackCommonRespVo.getCode() != 0) {
            return error(meituanYudingMsgCallbackCommonRespVo.getMessage());
        }
        return meituanYudingMsgCallbackCommonRespVo;
    }
}
