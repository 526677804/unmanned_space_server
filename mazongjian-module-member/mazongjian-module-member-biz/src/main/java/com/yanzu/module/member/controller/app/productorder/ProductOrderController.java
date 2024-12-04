package com.yanzu.module.member.controller.app.productorder;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.productorder.vo.*;
import com.yanzu.module.member.service.productorder.ProductOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

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
    @PreAuthenticated
    public CommonResult<WxPayOrderRespVO> create(@RequestBody @Valid AppSaveOrderReqVo saveOrderReqVo) {
        return success(productOrderService.createOrder(saveOrderReqVo));
    }

    @PostMapping("/cancel")
    @Operation(summary = "订单取消")
    @PreAuthenticated
    public CommonResult<Boolean> cancelPay(@RequestBody AppCancelPayReqVo reqVo) {
        productOrderService.cancelPay(reqVo.getOrderNo());
        return success(true);
    }

    @PostMapping("/page")
    @Operation(summary = "用户获取订单分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppUserOrderPageRespVo>> getOrderPage(@RequestBody AppUserOrderPageReqVo reqVo){
        return success(productOrderService.userOrderByPage(reqVo));
    }

    @GetMapping("/getstore")
    @Operation(summary = "获取在哪些门店下过单")
    @PreAuthenticated
    public CommonResult<List<AppHaveOrderStoreRespVo>> selectHaveOrderStore(){
        return success(productOrderService.selectHaveOrderStore());
    }

    @PostMapping("/pay/{id}")
    @Operation(summary = "商品订单列表-支付金额")
    @PreAuthenticated
    public CommonResult<WxPayOrderRespVO> create(@PathVariable Long id) {
        return success(productOrderService.pay(id));
    }

    @PostMapping("/manage/page")
    @Operation(summary = "管理员获取管理门店下的订单")
    @PreAuthenticated
    public CommonResult<PageResult<AppUserOrderPageRespVo>> manageProductOrder(@RequestBody AppUserOrderPageReqVo reqVo){
        return success(productOrderService.managerProductOrder(reqVo));
    }

    @PostMapping("/finish/{id}")
    @Operation(summary = "订单取消")
    @PreAuthenticated
    public CommonResult<Boolean> cancelPay(@PathVariable Long id) {
        productOrderService.finishOrder(id);
        return success(true);
    }




}
