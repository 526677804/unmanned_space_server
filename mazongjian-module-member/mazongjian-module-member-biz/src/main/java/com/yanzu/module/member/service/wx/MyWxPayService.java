package com.yanzu.module.member.service.wx;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;
import com.yanzu.module.member.dal.mysql.member.StoreWxpayConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.STORE_WX_PAY_CONFIG_NOT_FOUND;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.wx
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/22 14:17
 */
@Component
public class MyWxPayService {


    @Autowired
    private StoreWxpayConfigMapper storeWxpayConfigMapper;

    @Value("${wx.pay.appId}")
    private String appId;
    @Value("${wx.pay.mchId}")
    private String mchId;
    @Value("${wx.pay.mchKey}")
    private String mchKey;
    @Value("${wx.pay.keyPath}")
    private String keyPath;
    @Value("${wx.pay.privateKeyPath}")
    private String privateKeyPath;
    @Value("${wx.pay.privateCertPath}")
    private String privateCertPath;
    @Value("${wx.pay.returnUrl}")
    private String returnUrl;

    public WxPayService init(Long storeId) {
        if (ObjectUtils.isEmpty(storeId)) {
            throw exception(STORE_WX_PAY_CONFIG_NOT_FOUND);
        }
        StoreWxpayConfigDO config = storeWxpayConfigMapper.getConfigByStoreId(storeId);
        if (ObjectUtils.isEmpty(config)) {
            throw exception(STORE_WX_PAY_CONFIG_NOT_FOUND);
        }
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);//服务商的商户号
        payConfig.setMchKey(mchKey);//服务商的v2秘钥
        payConfig.setKeyPath(keyPath);//服务商的证书文件
        payConfig.setPrivateCertPath(privateCertPath);//服务商的证书文件
        payConfig.setPrivateKeyPath(privateKeyPath);//服务商的证书文件
        payConfig.setSubAppId(config.getAppId());//服务商模式下的子商户公众账号ID
        payConfig.setSubMchId(config.getMchId());//服务商模式下的子商户号
        payConfig.setTradeType("JSAPI");
        payConfig.setNotifyUrl(returnUrl);
//        payConfig.setPrivateKeyString(config.getApiclientKey());
//        payConfig.setPrivateCertString(config.getApiclientCert());
        // weixin-pay-java 无法设置内容，只允许读取文件，所以这里要创建临时文件来解决
//        if (Base64.isBase64(config.getKeyContent())) {
//            payConfig.setKeyPath(FileUtils.createTempFile(Base64.decode(config.getKeyContent())).getPath());
//        }
//        if (StrUtil.isNotEmpty(config.getPrivateKeyContent())) {
//            payConfig.setPrivateKeyPath(FileUtils.createTempFile(config.getPrivateKeyContent()).getPath());
//        }
//        if (StrUtil.isNotEmpty(config.getPrivateCertContent())) {
//            payConfig.setPrivateCertPath(FileUtils.createTempFile(config.getPrivateCertContent()).getPath());
//        }

//        payConfig.setSubAppId(StringUtils.trimToNull(config.getSubAppId()));
//        payConfig.setSubMchId(StringUtils.trimToNull(config.getSubMchId()));
//        payConfig.setKeyPath(StringUtils.trimToNull(config.getKeyPath()));
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

}
