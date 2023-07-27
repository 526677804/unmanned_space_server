package com.yanzu.module.member.controller.app.order;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.LoginUser;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.admin.orderinfo.vo.OrderInfoRespVO;
import com.yanzu.module.member.controller.app.order.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static redis.clients.jedis.util.JedisURIHelper.getUser;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 14:25
 */
@Tag(name = "miniapp - 我的订单")
@RestController
@RequestMapping("/member/order")
@Validated
@Slf4j
public class OrderController {


    @PostMapping("/save")
    @Operation(summary = "提交订单", description = "下单使用")
    @PreAuthenticated
    public CommonResult<Boolean> save(@RequestBody @Valid OrderSaveReqVO reqVO) {
        return null;
    }

    @PostMapping("/renew")
    @Operation(summary = "订单续费", description = "订单详情/订单管理使用")
    @PreAuthenticated
    public CommonResult<Boolean> renew(@RequestBody @Valid OrderRenewalReqVO reqVO) {
        return null;
    }


    @PostMapping("/getOrderPage")
    @Operation(summary = "获取订单列表分页", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<PageResult<OrderListRespVO>> getOrderPage(@RequestBody @Valid OrderPageReqVO reqVO) {
        return null;
    }

    @GetMapping("/getOrderInfo/{orderId}")
    @Operation(summary = "获取订单详情", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<OrderInfoAppRespVO> getOrderInfo(@PathVariable("orderId") Long orderId) {
        return null;
    }

    @PostMapping("/changeStatus")
    @Operation(summary = "修改订单状态", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<Boolean> changeStatus(@RequestBody @Valid OrderPageReqVO reqVO) {
        return null;
    }


    @PutMapping("/openStoreDoor/{orderId}")
    @Operation(summary = "(开关)门店的大门", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<Boolean> openStoreDoor(@PathVariable("orderId") Long orderId) {
        return null;
    }

    @PutMapping("/openRoomDoor/{orderId}")
    @Operation(summary = "(开关)房间的大门", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<Boolean> openRoomDoor(@PathVariable("orderId") Long orderId) {
        return null;
    }

    @GetMapping("/getRoomImgs/{roomId}")
    @Operation(summary = "获取房间的图片组", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<List<String>> getRoomImgs(@PathVariable("roomId") Long roomId) {
        return null;
    }

    @GetMapping("/getRoomList/{orderId}")
    @Operation(summary = "更换房间-获取房间列表", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<OrderRoomListRespVO> orderRenewal(@PathVariable("orderId") Long orderId) {
        return null;
    }

    @PostMapping("/changeRoom/{orderId}/{roomId}")
    @Operation(summary = "更换房间 - 提交更换", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<OrderRoomListRespVO> orderRenewal(@PathVariable("orderId") Long orderId, @PathVariable("roomId") Long roomId) {
        return null;
    }


}
