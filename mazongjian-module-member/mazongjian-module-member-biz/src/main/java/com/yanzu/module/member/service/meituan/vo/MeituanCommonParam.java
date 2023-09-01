package com.yanzu.module.member.service.meituan.vo;

import com.yanzu.framework.common.util.date.DateUtils;
import lombok.Data;

import java.util.Date;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.meituan
 * @DESCRIPTION: 美团 公共请求参数
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/9/1 13:48
 */
@Data
public class MeituanCommonParam {

    //点评到综开放平台分配给应用的AppKey。
    private String app_key;

    //时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8，例如：2017-01-01 12:00:00。服务端允许客户端请求最大时间误差为30分钟。
    private String timestamp = DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND);

    //商家授权成功后，点评到综开放平台颁发给应用的授权信息。当对接模块的标签上注明：“需要授权”，则此参数必传；“不需要授权”，则此参数不需要传
    private String session;

    //响应格式。默认为json格式。
    private String format = "json";

    //API协议版本，默认值：1，此后版本升级，会递增。
    private String v = "1";

    //签名的摘要算法，默认值为：MD5。
    private String sign_method = "MD5";

    //API输入参数签名结果。
    private String sign;


}
