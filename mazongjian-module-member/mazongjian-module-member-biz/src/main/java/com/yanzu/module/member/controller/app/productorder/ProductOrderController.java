package com.yanzu.module.member.controller.app.productorder;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.productorder.vo.AppCancelPayReqVo;
import com.yanzu.module.member.controller.app.productorder.vo.AppSaveOrderReqVo;
import com.yanzu.module.member.service.productorder.ProductOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yanzu.framework.common.pojo.CommonResult.success;

@Tag(name = "商品订单")
@RestController
@RequestMapping("/product/order")
@Validated
public class ProductOrderController {

    @Resource
    private ProductOrderService productOrderService;

    @PostMapping("/create")
    @Operation(summary = "订单创建")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<WxPayOrderRespVO> create(@RequestBody @Valid AppSaveOrderReqVo saveOrderReqVo) {
        return success(productOrderService.createOrder(saveOrderReqVo));
    }

    @PostMapping("/cancel")
    @Operation(summary = "订单创建")
    public CommonResult<Boolean> cancelPay(@RequestBody AppCancelPayReqVo reqVo) {
        productOrderService.cancelPay(reqVo.getOrderNo());
        return success(true);
    }

}
