package com.yanzu.module.system.api.sms;

import com.yanzu.module.system.api.sms.dto.code.SmsCodeValidateReqDTO;
import com.yanzu.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import com.yanzu.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import com.yanzu.module.system.service.sms.SmsCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 短信验证码 API 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SmsCodeApiImpl implements SmsCodeApi {

    @Resource
    private SmsCodeService smsCodeService;

    /**
     * 验证码的开关，默认为 true
     */
    @Value("${mazongjian.captcha.enable:true}")
    private Boolean captchaEnable;

    @Override
    public void sendSmsCode(SmsCodeSendReqDTO reqDTO) {
        smsCodeService.sendSmsCode(reqDTO);
    }

    @Override
    public void useSmsCode(SmsCodeUseReqDTO reqDTO) {
        if(captchaEnable){
            smsCodeService.useSmsCode(reqDTO);
        }
    }

    @Override
    public void validateSmsCode(SmsCodeValidateReqDTO reqDTO) {
        smsCodeService.validateSmsCode(reqDTO);
    }

}
