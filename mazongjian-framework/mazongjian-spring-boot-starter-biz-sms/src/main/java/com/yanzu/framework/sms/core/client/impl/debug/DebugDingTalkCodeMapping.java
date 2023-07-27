package com.yanzu.framework.sms.core.client.impl.debug;

import com.yanzu.framework.common.exception.ErrorCode;
import com.yanzu.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.yanzu.framework.sms.core.client.SmsCodeMapping;
import com.yanzu.framework.sms.core.enums.SmsFrameworkErrorCodeConstants;

import java.util.Objects;

/**
 * 钉钉的 SmsCodeMapping 实现类
 *
 * @author 芋道源码
 */
public class DebugDingTalkCodeMapping implements SmsCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        return Objects.equals(apiCode, "0") ? GlobalErrorCodeConstants.SUCCESS : SmsFrameworkErrorCodeConstants.SMS_UNKNOWN;
    }

}
