package com.yanzu.module.member.service.iot.ewlbean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

@Data
public class EwlSwitchReqVO {

    private String action = "update";//固定参数: update
    private String deviceid;//设备 ID
    private String apikey;//用户 apikey
    private String userAgent = "app";//app 或者 device
    private String sequence;//时间戳精确到毫秒
    private JSONObject params;
//    private long ts;//时间戳精确到毫秒


    public EwlSwitchReqVO() {
        long time = new Date().getTime();
        this.sequence = String.valueOf(time);
//        this.ts = time;
    }
}
