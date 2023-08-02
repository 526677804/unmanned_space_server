package com.yanzu.module.member.controller.app.order;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.order.vo.*;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.order.AppOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.common.pojo.CommonResult.success;
import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yanzu.module.member.enums.ErrorCodeConstants.ORDER_PAGE_PARAM_ERROR;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 14:25
 */
@Tag(name = "miniapp - 我的订单/下单/续费")
@RestController
@RequestMapping("/member/order")
@Validated
@Slf4j
public class OrderController {

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private DeviceService deviceService;

    @PostMapping("/preOrder")
    @Operation(summary = "预下单,调用此接口用于判断当前是否能提交订单或者续费,如果可以下单会返回订单应付价格", description = "下单使用")
    @PreAuthenticated
    public CommonResult<BigDecimal> preOrder(@RequestBody @Valid OrderPreReqVO reqVO) {
        return success(appOrderService.preOrder(reqVO.getRoomId(), reqVO.getStartTime(), reqVO.getEndTime(), reqVO.getCouponId(), null));
    }

    @PostMapping("/save")
    @Operation(summary = "提交订单", description = "下单使用")
    @PreAuthenticated
    @Idempotent(timeout = 5, timeUnit = TimeUnit.SECONDS, message = "请勿重复提交")
    public CommonResult<Boolean> save(@RequestBody @Valid OrderSaveReqVO reqVO) {
        appOrderService.save(reqVO);
        return success(true);
    }

    @PostMapping("/renew")
    @Operation(summary = "订单续费", description = "订单详情/订单管理使用")
    @PreAuthenticated
    @Idempotent(timeout = 5, timeUnit = TimeUnit.SECONDS, message = "请勿重复提交")
    public CommonResult<Boolean> renew(@RequestBody @Valid OrderRenewalReqVO reqVO) {
        appOrderService.renew(reqVO);
        return success(true);
    }


    @PostMapping("/getOrderPage")
    @Operation(summary = "获取订单列表分页", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<PageResult<OrderListRespVO>> getOrderPage(@RequestBody @Valid OrderPageReqVO reqVO) {
        //参数检查
        if (!ObjectUtils.isEmpty(reqVO.getOrderColumn())) {
            if (reqVO.getOrderColumn().equals("createTime") || reqVO.getOrderColumn().equals("startTime")) {
                //正确

            } else {
                throw exception(ORDER_PAGE_PARAM_ERROR);
            }
        }
        reqVO.setUserId(getLoginUserId());
        return success(appOrderService.getOrderPage(reqVO));
    }

    @GetMapping("/getOrderInfo/{orderId}")
    @Operation(summary = "获取订单详情", description = "我的订单使用")
    @PreAuthenticated
    @Parameter(name = "orderId")
    public CommonResult<OrderInfoAppRespVO> getOrderInfo(@PathVariable("orderId") Long orderId) {
        return success(appOrderService.getOrderInfo(orderId));
    }

    @PutMapping("/startOrder/{orderId}")
    @Operation(summary = "开始订单", description = "我的订单使用")
    @PreAuthenticated
    @Parameter(name = "orderId")
    @Idempotent(timeout = 5, timeUnit = TimeUnit.SECONDS, message = "请勿重复提交")
    public CommonResult<Boolean> startOrder(@PathVariable("orderId") Long orderId) {
        appOrderService.startOrder(orderId);
        return success(true);
    }

    @PutMapping("/cancelOrder/{orderId}")
    @Operation(summary = "取消订单 ", description = "我的订单使用")
    @PreAuthenticated
    @Parameter(name = "orderId")
    @Idempotent(timeout = 5, timeUnit = TimeUnit.SECONDS, message = "请勿重复提交")
    public CommonResult<Boolean> cancelOrder(@PathVariable("orderId") Long orderId) {
        appOrderService.cancelOrder(orderId);
        return success(true);
    }

//    @PutMapping("/closeOrder/{orderId}")
//    @Operation(summary = "提前结束订单 ", description = "我的订单使用")
//    @PreAuthenticated
//    @Parameter(name = "orderId")
//    @Idempotent(timeout = 5, timeUnit = TimeUnit.SECONDS, message = "请勿重复提交")
//    public CommonResult<Boolean> closeOrder(@PathVariable("orderId") Long orderId) {
//        appOrderService.closeOrder(orderId);
//        return success(true);
//    }

    @PutMapping("/openStoreDoor/{orderId}")
    @Operation(summary = "(开关)门店的大门", description = "我的订单使用")
    @PreAuthenticated
    @Parameter(name = "orderId")
    @Idempotent(timeout = 5, timeUnit = TimeUnit.SECONDS, message = "请勿重复提交")
    public CommonResult<Boolean> openStoreDoor(@PathVariable("orderId") Long orderId) {
        deviceService.openStoreDoor(null, orderId, 1);
        return success(true);
    }

    @PutMapping("/openRoomDoor/{orderId}")
    @Operation(summary = "(开关)房间的大门", description = "我的订单使用")
    @PreAuthenticated
    @Parameter(name = "orderId")
    @Idempotent(timeout = 5, timeUnit = TimeUnit.SECONDS, message = "请勿重复提交")
    public CommonResult<Boolean> openRoomDoor(@PathVariable("orderId") Long orderId) {
        deviceService.openRoomDoor(null, orderId, 1);
        return success(true);
    }

    @GetMapping("/getRoomImgs/{roomId}")
    @Operation(summary = "获取房间的图片组 逗号分隔", description = "我的订单使用")
    @PreAuthenticated
    @Parameter(name = "roomId")
    public CommonResult<String> getRoomImgs(@PathVariable("roomId") Long roomId) {
        return success(appOrderService.getRoomImgs(roomId));
    }

    @PutMapping("/changeRoom/{orderId}/{roomId}")
    @Operation(summary = "更换房间 - 提交更换", description = "我的订单使用")
    @PreAuthenticated
    @Parameter(name = "orderId")
    @Parameter(name = "roomId")
    @Idempotent(timeout = 5, timeUnit = TimeUnit.SECONDS, message = "请勿重复提交")
    public CommonResult<Boolean> changeRoom(@PathVariable("orderId") Long orderId, @PathVariable("roomId") Long roomId) {
        appOrderService.changeRoom(orderId, roomId);
        return success(true);
    }


}
