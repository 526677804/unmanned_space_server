package com.yanzu.module.member.controller.app.callback;

import com.alibaba.fastjson.JSONObject;
import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import com.yanzu.module.member.service.device.DeviceService;
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
    private DeviceService deviceService;


    @PostMapping("/wxpay/update")
    @Operation(summary = "更新订单为已支付")
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    @Parameter(name = "xmlData")
    public String updateOrder(@RequestBody String xmlData) {
        return payOrderService.updateOrder(xmlData);
    }


    @PostMapping("/wxpay/urefunded")
    @Operation(summary = "更新订单为已退款")
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public String updateOrderRefunded(@RequestParam(required = false) Map<String, String> params,
                                      @RequestBody(required = false) String body) {
        return payOrderService.updateOrderRefunded(params, body);
    }

    @RequestMapping(value = "/meituan", method = {RequestMethod.GET, RequestMethod.HEAD})
    @Operation(summary = "美团授权回调")
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public String meituan(@RequestParam("auth_code") String auth_code, @RequestParam("state") String state) {
        return meituanService.getToken(auth_code, state);
    }

    @PostMapping(value = "/weimenjin")
    @Operation(summary = "微门禁回调")
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public void weimenjin(@RequestBody(required = false) JSONObject body) {
        log.info("收到智能硬件回调:{}",body);
        System.out.println(body);
         deviceService.weimenjin(body);
    }
}
