//package com.yanzu.module.member.pay;
//
//import com.github.binarywang.wxpay.service.WxPayService;
//import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConditionalOnClass(WxPayService.class)
//public class MyWxPayConfig {
//
////    @Autowired
////    private WxPayProperties properties;
//
//    @Bean
//    @ConditionalOnMissingBean
//    public WxPayService wxService() {
////        WxPayConfig payConfig = new WxPayConfig();
////        payConfig.setAppId(StringUtils.trimToNull(this.properties.getAppId()));
////        payConfig.setMchId(StringUtils.trimToNull(this.properties.getMchId()));
////        payConfig.setMchKey(StringUtils.trimToNull(this.properties.getMchKey()));
////        payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId()));
////        payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId()));
////        payConfig.setKeyPath(StringUtils.trimToNull(this.properties.getKeyPath()));
////
////        // 可以指定是否使用沙箱环境
////        payConfig.setUseSandboxEnv(false);
////
////        WxPayService wxPayService = new WxPayServiceImpl();
////        wxPayService.setConfig(payConfig);
////        return wxPayService;
//
//        return new WxPayServiceImpl();
//    }
//}
