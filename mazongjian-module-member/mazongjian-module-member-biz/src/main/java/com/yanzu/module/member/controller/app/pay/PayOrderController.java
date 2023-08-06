package com.yanzu.module.member.controller.app.pay;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import com.yanzu.module.member.controller.app.pay.vo.PayOrderNotifyReqDTO;
import com.yanzu.module.member.controller.app.pay.vo.PayRefundNotifyReqDTO;
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

import static com.yanzu.framework.common.pojo.CommonResult.success;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 14:25
 */
@Tag(name = "miniapp - 支付回调")
@RestController
@RequestMapping("/wx/pay")
@Validated
@Slf4j
public class PayOrderController {


    @Resource
    private PayOrderService payOrderService;


    @PostMapping("/update")
    @Operation(summary = "更新订单为已支付")
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public CommonResult<Boolean> updateOrder(@RequestParam(required = false) Map<String, String> params,
                                             @RequestBody(required = false) String body) {
        payOrderService.updateOrder(params, body);
        return success(true);
    }


    @PostMapping("/urefunded")
    @Operation(summary = "更新订单为已退款")
    @PermitAll // 无需登录，安全由 PayDemoOrderService 内部校验实现
    @OperateLog(enable = false) // 禁用操作日志，因为没有操作人
    public CommonResult<Boolean> updateOrderRefunded(@RequestParam(required = false) Map<String, String> params,
                                                     @RequestBody(required = false) String body) {
        payOrderService.updateOrderRefunded(params, body);
        return success(true);
    }
}
