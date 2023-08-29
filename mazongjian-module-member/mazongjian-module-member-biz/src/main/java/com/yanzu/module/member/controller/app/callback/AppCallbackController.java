package com.yanzu.module.member.controller.app.callback;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import com.yanzu.module.member.service.payorder.PayOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import java.util.Map;

import static com.yanzu.framework.common.pojo.CommonResult.success;

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

    @PostMapping("/wxpay/update")
    @Operation(summary = "更新订单为已支付")
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public String updateOrder(@RequestParam(required = false) Map<String, String> params,
                              @RequestBody(required = false) String body) {
        return payOrderService.updateOrder(params, body);
    }


    @PostMapping("/wxpay/urefunded")
    @Operation(summary = "更新订单为已退款")
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public String updateOrderRefunded(@RequestParam(required = false) Map<String, String> params,
                                      @RequestBody(required = false) String body) {
        return payOrderService.updateOrderRefunded(params, body);
    }
}
