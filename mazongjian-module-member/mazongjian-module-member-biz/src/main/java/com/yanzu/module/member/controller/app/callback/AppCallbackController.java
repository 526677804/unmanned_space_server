package com.yanzu.module.member.controller.app.callback;

import com.alibaba.fastjson.JSONObject;
import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import com.yanzu.module.member.service.iot.IotDeviceService;
import com.yanzu.module.member.service.meituan.MeituanService;
import com.yanzu.module.member.service.payorder.PayOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 14:25
 */
@Tag(name = "miniapp - 回调")
@RestController
@RequestMapping("/callback")
@Validated
@Slf4j
public class AppCallbackController {

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private MeituanService meituanService;

    @Resource
    private IotDeviceService iotDeviceService;


    @PostMapping("/wxpay/update")
    @Operation(summary = "微信支付回调")
    @PermitAll // 无需登录
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    @Parameter(name = "xmlData")
    public String updateOrder(@RequestBody String xmlData) {
        return payOrderService.updateOrder(xmlData);
    }


    @PostMapping("/wxpay/urefunded")
    @Operation(summary = "微信退款回调")
    @PermitAll // 无需登录
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public String updateOrderRefunded(@RequestParam(required = false) Map<String, String> params,
                                      @RequestBody(required = false) String body) {
        return payOrderService.updateOrderRefunded(params, body);
    }

    @RequestMapping(value = "/meituan", method = {RequestMethod.GET, RequestMethod.HEAD})
    @Operation(summary = "美团授权回调")
    @PermitAll // 无需登录
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public String meituan(@RequestParam("auth_code") String auth_code, @RequestParam("state") String state) {
        return meituanService.getToken(auth_code, state);
    }

    @PostMapping(value = "/iotCallback")
    @Operation(summary = "物联网平台回调")
    @PermitAll // 无需登录
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public void iotCallback(@RequestBody JSONObject json) {
        log.info("收到物联网平台回调,params:{}", json);
        iotDeviceService.iotPlatform(json);
    }



}
