package com.yanzu.module.member.controller.app.manager;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.admin.user.vo.AppUserRechargeReqVO;
import com.yanzu.module.member.controller.app.clear.vo.AppClearPageReqVO;
import com.yanzu.module.member.controller.app.clear.vo.AppClearPageRespVO;
import com.yanzu.module.member.controller.app.manager.vo.*;
import com.yanzu.module.member.controller.app.order.vo.OrderListRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderPageReqVO;
import com.yanzu.module.member.controller.app.order.vo.OrderRenewalReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageRespVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageRespVO;
import com.yanzu.module.member.service.manager.AppMangerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.concurrent.TimeUnit;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.common.pojo.CommonResult.success;
import static com.yanzu.module.member.enums.ErrorCodeConstants.ORDER_PAGE_PARAM_ERROR;

@Tag(name = "miniapp - 管理员角色使用（订单、会员、优惠券、保洁管理）相关接口")
@RestController
@RequestMapping("/member/manager")
@Validated
@Slf4j
public class AppManagerController {

    @Resource
    private AppMangerService appMangerService;


    @PostMapping("/getOrderPage")
    @Operation(summary = "获取订单列表分页", description = "管理员订单管理使用")
    @PreAuthenticated
    public CommonResult<PageResult<OrderListRespVO>> getOrderPage(@RequestBody @Valid OrderPageReqVO reqVO) {
        //参数检查
        if (!ObjectUtils.isEmpty(reqVO.getOrderColumn())) {
            if (reqVO.getOrderColumn().equals("createTime") || reqVO.getOrderColumn().equals("startTime")) {
                //参数正确

            } else {
                throw exception(ORDER_PAGE_PARAM_ERROR);
            }
        }
        return success(appMangerService.getOrderPage(reqVO));
    }

    @PostMapping("/getMemberPage")
    @Operation(summary = "获取会员分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppMemberPageRespVO>> getMemberPage(@RequestBody @Valid AppMemberPageReqVO reqVO) {
        return success(appMangerService.getMemberPage(reqVO));
    }


    @PostMapping("/getCouponPage")
    @Operation(summary = "管理员获取优惠券分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppCouponPageRespVO>> getCouponPage(@RequestBody @Valid AppManagerCouponPageReqVO reqVO) {
        return success(appMangerService.getCouponPage(reqVO));
    }

    @PostMapping("/giftCoupon")
    @Operation(summary = "管理员赠送优惠券")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> giftCoupon(@RequestBody @Valid AppGiftCouponReqVO reqVO) {
        appMangerService.giftCoupon(reqVO);
        return success(true);
    }

    @GetMapping("/getCouponDetail/{couponId}")
    @Operation(summary = "管理员获取优惠券详情")
    @PreAuthenticated
    @Parameter(name = "couponId")
    public CommonResult<AppCouponDetailRespVO> getCouponDetail(@PathVariable("couponId") Long couponId) {
        return success(appMangerService.getCouponDetail(couponId));
    }

    @PostMapping("/renewByAdmin")
    @Operation(summary = "订单续费", description = "管理员订单管理使用")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> renew(@RequestBody @Valid OrderRenewalReqVO reqVO) {
        appMangerService.renew(reqVO);
        return success(true);
    }

    @PostMapping("/changeOrder")
    @Operation(summary = "订单修改", description = "管理员订单管理使用")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> changeOrder(@RequestBody @Valid OrderChangeReqVO reqVO) {
        appMangerService.changeOrder(reqVO);
        return success(true);
    }

    @PostMapping("/getClearManagerPage")
    @Operation(summary = "管理员获取保洁任务分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppClearPageRespVO>> getClearManagerPage(@RequestBody @Valid AppClearPageReqVO reqVO) {
        return success(appMangerService.getClearManagerPage(reqVO));
    }

    @PostMapping("/cancelOrder/{orderId}")
    @Operation(summary = "管理员取消订单 ", description = "订单管理使用")
    @PreAuthenticated
    @Parameter(name = "orderId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> cancelOrder(@PathVariable("orderId") Long orderId) {
        appMangerService.cancelOrder(orderId,false);
        return success(true);
    }


    @PostMapping("/refundOrder/{orderId}")
    @Operation(summary = "管理员退款订单 ", description = "订单管理使用")
    @PreAuthenticated
    @Parameter(name = "orderId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> refundOrder(@PathVariable("orderId") Long orderId) {
        appMangerService.cancelOrder(orderId,true);
        return success(true);
    }

    @PostMapping("/saveCouponDetail")
    @Operation(summary = "管理员保存优惠券详情")
    @PreAuthenticated
    public CommonResult<Boolean> saveCouponDetail(@RequestBody @Valid AppCouponDetailReqVO reqVO) {
        appMangerService.saveCouponDetail(reqVO);
        return success(true);
    }


    @PostMapping("/getClearUserPage")
    @Operation(summary = "管理员获取保洁员分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppClearUserPageRespVO>> getClearUserPage(@RequestBody @Valid AppClearUserPageReqVO reqVO) {
        return success(appMangerService.getClearUserPage(reqVO));
    }

    @PostMapping("/getAdminUserPage")
    @Operation(summary = "管理员获取管理员分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppAdminUserPageRespVO>> getAdminUserPage(@RequestBody @Valid AppClearUserPageReqVO reqVO) {
        return success(appMangerService.getAdminUserPage(reqVO));
    }

    @PostMapping("/deleteAdminUser/{storeId}/{userId}")
    @Operation(summary = "删除管理员")
    @PreAuthenticated
    @Parameter(name = "storeId")
    @Parameter(name = "userId")
    public CommonResult<Boolean> deleteAdminUser(@PathVariable("storeId") Long storeId, @PathVariable("userId") Long userId) {
        appMangerService.deleteAdminUser(storeId, userId);
        return success(true);
    }

    @PostMapping("/saveAdminUser")
    @Operation(summary = "保存管理员信息")
    @PreAuthenticated
    public CommonResult<Boolean> saveAdminUser(@RequestBody @Valid AppClearUserDetailReqVO reqVO) {
        appMangerService.saveAdminUser(reqVO);
        return success(true);
    }

    @PostMapping("/deleteClearUser/{storeId}/{userId}")
    @Operation(summary = "管理员删除保洁员")
    @PreAuthenticated
    @Parameter(name = "storeId")
    @Parameter(name = "userId")
    public CommonResult<Boolean> deleteClearUser(@PathVariable("storeId") Long storeId, @PathVariable("userId") Long userId) {
        appMangerService.deleteClearUser(storeId, userId);
        return success(true);
    }

    @PostMapping("/saveClearUser")
    @Operation(summary = "管理员保存保洁员信息")
    @PreAuthenticated
    public CommonResult<Boolean> saveClearUser(@RequestBody @Valid AppClearUserDetailReqVO reqVO) {
        appMangerService.saveClearUser(reqVO);
        return success(true);
    }

    @PostMapping("/settlementClearUser")
    @Operation(summary = "管理员结算保洁员费用")
    @PreAuthenticated
    public CommonResult<Boolean> settlementClearUser(@RequestBody @Valid AppSettlementClearUserReqVO reqVO) {
        appMangerService.settlementClearUser(reqVO);
        return success(true);
    }


    @PostMapping("/complaintClearInfo")
    @Operation(summary = "管理员驳回/撤销驳回保洁员订单")
    @PreAuthenticated
    public CommonResult<Boolean> complaintClearInfo(@RequestBody @Valid AppComplaintClearInfoReqVO reqVO) {
        appMangerService.complaintClearInfo(reqVO);
        return success(true);
    }

    @PostMapping("/applyWithdrawal")
    @Operation(summary = "申请提现")
    @PreAuthenticated
    public CommonResult<Boolean> applyWithdrawal() {
        appMangerService.applyWithdrawal();
        return success(true);
    }

    @PostMapping("/getWithdrawalPage")
    @Operation(summary = "获取提现记录分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppWithdrawalPageRespVO>> getWithdrawalPage(@RequestBody @Validated AppWithdrawalPageReqVO reqVO) {
        return success(appMangerService.getWithdrawalPage(reqVO));
    }

    @PostMapping("/useGroupNo")
    @Operation(summary = "管理员团购验券")
    @PreAuthenticated
    public CommonResult<Boolean> useGroupNo(@RequestBody @Validated AppUseGroupNoReqVO reqVO) {
        appMangerService.useGroupNo(reqVO);
        return success(true);
    }

    @PostMapping("/changeOrderUser")
    @Operation(summary = "管理员修改订单用户")
    @PreAuthenticated
    public CommonResult<Boolean> changeOrderUser(@RequestBody @Validated AppChangeOrderUserReqVO reqVO) {
        appMangerService.changeOrderUser(reqVO);
        return success(true);
    }

    @PostMapping("/submitOrder")
    @Operation(summary = "管理员提交订单", description = "下单使用")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Long> submitOrder(@RequestBody @Valid OrderSubmitReqVO reqVO) {
        return success(appMangerService.submitOrder(reqVO));
    }

    @PostMapping("/recharge")
    @Operation(summary = "管理员给用户余额充值")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> recharge(@Valid @RequestBody AppUserRechargeReqVO reqVO) {
        appMangerService.recharge(reqVO);
        return success(true);
    }

    @PostMapping("/cancelClear/{clearId}")
    @Operation(summary = "取消保洁订单")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    @Parameter(name = "clearId",description = "保洁订单id")
    public CommonResult<Boolean> cancelClear(@PathVariable("clearId")Long clearId) {
        appMangerService.cancelClear(clearId);
        return success(true);
    }

//    @PostMapping("/changeOrderTime")
//    @Operation(summary = "管理员修改订单时间")
//    @PreAuthenticated
//    public CommonResult<Boolean> changeOrderTime(@RequestBody @Validated AppChangeOrderTimeReqVO reqVO) {
//        appMangerService.changeOrderTime(reqVO);
//        return success(true);
//    }

}
