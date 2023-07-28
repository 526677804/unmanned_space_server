package com.yanzu.module.member.controller.app.user;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.user.vo.*;
import com.yanzu.module.member.service.user.AppUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.common.pojo.CommonResult.success;
import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yanzu.module.infra.enums.ErrorCodeConstants.FILE_IS_EMPTY;

@Tag(name = "miniapp - 个人中心")
@RestController
@RequestMapping("/member/user")
@Validated
@Slf4j
public class AppUserController {

    @Resource
    private AppUserService userService;

    @PutMapping("/updateNickname")
    @Operation(summary = "修改用户昵称")
    @PreAuthenticated
    public CommonResult<Boolean> updateUserNickname(@RequestParam("nickname") String nickname) {
        userService.updateUserNickname(getLoginUserId(), nickname);
        return success(true);
    }

    @PostMapping("/updateAvatar")
    @Operation(summary = "修改用户头像")
    @PreAuthenticated
    public CommonResult<String> updateUserAvatar(@RequestParam("avatarFile") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_IS_EMPTY);
        }
        String avatar = userService.updateUserAvatar(getLoginUserId(), file.getInputStream());
        return success(avatar);
    }

    @PostMapping("/update-mobile")
    @Operation(summary = "修改用户手机")
    @PreAuthenticated
    public CommonResult<Boolean> updateMobile(@RequestBody @Valid AppUserUpdateMobileReqVO reqVO) {
        userService.updateUserMobile(getLoginUserId(), reqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得基本信息")
    @PreAuthenticated
    public CommonResult<AppUserInfoRespVO> getUserInfo() {
        return success(userService.getUserInfo(getLoginUserId()));
    }

    @PostMapping("/getBalancePage")
    @Operation(summary = "获取用户账单明细分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppUserMoneyBillRespVO>> getOrderPage(@RequestBody @Valid AppUserMoneyBillPageReqVO reqVO) {
        return success(userService.getOrderPage(reqVO));
    }

    @GetMapping("/getGiftBalanceList")
    @Operation(summary = "获取赠送余额列表")
    @PreAuthenticated
    public CommonResult<List<AppGiftBalanceListRespVO>> getGiftBalanceList() {
        return success(userService.getGiftBalanceList());
    }

    @PostMapping("/rechargeBalance")
    @Operation(summary = "用户余额充值")
    @PreAuthenticated
    public CommonResult<Boolean> eechargeBalance(@RequestBody @Valid AppRechargeBalanceReqVO reqVO) {
        userService.eechargeBalance(reqVO);
        return success(true);
    }

    @GetMapping("/getFranchiseInfo")
    @Operation(summary = "获取加盟信息")
    @PreAuthenticated
    public CommonResult<AppFranchiseInfoRespVO> getFranchiseInfo(HttpServletRequest request) {
        return success(userService.getFranchiseInfo(request));
    }


    @PostMapping("/saveFranchiseInfo")
    @Operation(summary = "提交加盟信息")
    @PreAuthenticated
    public CommonResult<Boolean> saveFranchiseInfo(@RequestBody @Valid AppFranchiseInfoReqVO reqVO) {
        userService.saveFranchiseInfo(reqVO);
        return success(true);
    }


    @PostMapping("/getCouponPage")
    @Operation(summary = "获取用户优惠券分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppCouponPageRespVO>> getCouponPage(@RequestBody @Valid AppCouponPageReqVO reqVO) {
        return success(userService.getCouponPage(reqVO));
    }


}

