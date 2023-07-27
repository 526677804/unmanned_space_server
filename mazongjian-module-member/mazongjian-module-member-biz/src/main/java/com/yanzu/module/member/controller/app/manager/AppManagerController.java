package com.yanzu.module.member.controller.app.manager;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.manager.vo.*;
import com.yanzu.module.member.controller.app.order.vo.OrderListRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageRespVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "miniapp - 管理员角色使用（订单、会员、优惠券、保洁管理）相关接口")
@RestController
@RequestMapping("/member/manager")
@Validated
@Slf4j
public class AppManagerController {

    @PostMapping("/getOrderPage")
    @Operation(summary = "获取订单列表分页", description = "我的订单使用")
    @PreAuthenticated
    public CommonResult<PageResult<OrderListRespVO>> getOrderPage(@RequestBody @Valid OrderPageReqVO reqVO) {
        return null;
    }

    @PostMapping("/getMemberPage")
    @Operation(summary = "获取会员分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppMemberPageRespVO>> getMemberPage(@RequestBody @Valid AppMemberPageReqVO reqVO) {
        return null;
    }

    @PostMapping("/getPresentCouponPage")
    @Operation(summary = "获取赠送优惠券分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppCouponPageRespVO>> getPresentCouponPage(@RequestBody @Valid AppPresentCouponPageReqVO reqVO) {
        return null;
    }

    @PostMapping("/getCouponPage")
    @Operation(summary = "管理员获取优惠券分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppCouponPageRespVO>> getCouponPage(@RequestBody @Valid AppManagerCouponPageReqVO reqVO) {
        return null;
    }


    @GetMapping("/getCouponDetail/{couponId}")
    @Operation(summary = "管理员获取优惠券详情")
    @PreAuthenticated
    public CommonResult<AppCouponDetailRespVO> getCouponDetail(@PathVariable("couponId") Long couponId) {
        return null;
    }


    @PostMapping("/saveCouponDetail")
    @Operation(summary = "管理员保存优惠券详情")
    @PreAuthenticated
    public CommonResult<Boolean> saveCouponDetail(@RequestBody @Valid AppCouponDetailReqVO reqVO) {
        return null;
    }


    @PostMapping("/getClearUserPage")
    @Operation(summary = "管理员获取保洁员分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppClearUserPageRespVO>> getClearUserPage(@RequestBody @Valid AppClearUserPageReqVO reqVO) {
        return null;
    }

    @PostMapping("/changeClearUserStatus/{userId}")
    @Operation(summary = "管理员修改保洁员状态 启用/禁用")
    @PreAuthenticated
    public CommonResult<Boolean> changeClearUserStatus(@PathVariable("userId") Long userId) {
        return null;
    }

    @PostMapping("/saveClearUser")
    @Operation(summary = "管理员保存保洁员信息")
    @PreAuthenticated
    public CommonResult<Boolean> saveClearUser(@RequestBody @Valid AppClearUserDetailReqVO reqVO) {
        return null;
    }

    @PostMapping("/settlementClearUser")
    @Operation(summary = "管理员结算保洁员费用")
    @PreAuthenticated
    public CommonResult<Boolean> settlementClearUser(@RequestBody @Valid AppSettlementClearUserReqVO reqVO) {
        return null;
    }


    @PostMapping("/complaintClearInfo")
    @Operation(summary = "管理员驳回/撤销驳回保洁员订单")
    @PreAuthenticated
    public CommonResult<Boolean> complaintClearInfo(@RequestBody @Valid AppComplaintClearInfoReqVO reqVO) {
        return null;
    }

}
