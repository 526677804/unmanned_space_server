package com.yanzu.module.member.service.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceHttpClientImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.codec.Base64;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.yanzu.framework.common.util.io.FileUtils;
import com.yanzu.framework.tenant.core.context.TenantContextHolder;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;
import com.yanzu.module.member.dal.mysql.member.StoreWxpayConfigMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MyWxService {


    @Autowired
    private StoreWxpayConfigMapper storeWxpayConfigMapper;


    //支付服务商模式的配置
    @Value("${wx.miniapp.appid}")
    private String appId;
    @Value("${wx.pay.mchId}")
    private String mchId;
    @Value("${wx.pay.mchKey}")
    private String mchKey;
    @Value("${wx.pay.keyPath}")
    private String keyPath;
    //支付回调地址
    @Value("${wx.pay.returnUrl}")
    private String returnUrl;

    public WxPayService initWxPay(Long storeId) {
        log.info("初始化门店：{}，微信支付", storeId);
        if (ObjectUtils.isEmpty(storeId)) {
            throw exception(STORE_WX_PAY_CONFIG_NOT_FOUND);
        }
        StoreWxpayConfigDO config = getWxPayConfig(storeId);
        if (ObjectUtils.isEmpty(config)) {
            throw exception(STORE_WX_PAY_CONFIG_NOT_FOUND);
        }
        MiniappConfigVO miniappConfigVO = getMiniAppConfig();
        WxPayConfig payConfig = new WxPayConfig();
        if (config.getServiceModel()) {
            //支付服务商模式
            payConfig.setAppId(appId);
            payConfig.setMchId(mchId);//服务商的商户号
            //服务商模式下的子商户号
            payConfig.setSubAppId(miniappConfigVO.getMiniappId());
            payConfig.setSubMchId(config.getMchId());
            payConfig.setMchKey(mchKey);//服务商的v2秘钥
            payConfig.setKeyPath(keyPath);//服务商的证书文件
        } else {
            //非服务商模式
            payConfig.setAppId(miniappConfigVO.getMiniappId());
            payConfig.setSubAppId(miniappConfigVO.getMiniappId());
            payConfig.setMchId(config.getMchId());//商户号
            payConfig.setMchKey(config.getMchKey());//v2秘钥
            // weixin-pay-java 无法设置内容，只允许读取文件，所以这里要创建临时文件来解决
            payConfig.setKeyPath(FileUtils.createTempFile(Base64.decode(config.getP12())).getPath());//证书文件
        }
        payConfig.setTradeType("JSAPI");
        payConfig.setNotifyUrl(returnUrl);
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }


    @SneakyThrows
    public WxPayMpOrderResult createOrder(WxPayService wxPayService, Long storeId, String orderNo, Integer payPrice, String openId) {
        StoreWxpayConfigDO config = getWxPayConfig(storeId);
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setBody("微信支付订单");
        wxPayUnifiedOrderRequest.setOutTradeNo(orderNo);
        wxPayUnifiedOrderRequest.setTotalFee(payPrice);
        wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
        wxPayUnifiedOrderRequest.setNotifyUrl(returnUrl);
        wxPayUnifiedOrderRequest.setTradeType("JSAPI");
        wxPayUnifiedOrderRequest.setProfitSharing(config.getServiceModel() && config.getSplit() ? "Y" : "N");
        if (config.getServiceModel()) {
            wxPayUnifiedOrderRequest.setSubOpenid(openId);
        } else {
            wxPayUnifiedOrderRequest.setOpenid(openId);
        }
        return wxPayService.createOrder(wxPayUnifiedOrderRequest);
    }


    public WxMaService initWxMa() {
        MiniappConfigVO miniAppConfig = getMiniAppConfig();
        WxMaService service = new WxMaServiceHttpClientImpl();
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(miniAppConfig.getMiniappId());
        config.setSecret(miniAppConfig.getMiniappSecret());
        service.setWxMaConfig(config);
        return service;
    }


    public StoreWxpayConfigDO getWxPayConfig(Long storeId) {
        return storeWxpayConfigMapper.getConfigByStoreId(storeId);
    }

    public MiniappConfigVO getMiniAppConfig() {
        return storeWxpayConfigMapper.getMiniappConfig(TenantContextHolder.getTenantId());
    }
}
