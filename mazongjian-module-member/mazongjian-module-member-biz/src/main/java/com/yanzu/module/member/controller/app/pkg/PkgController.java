package com.yanzu.module.member.controller.app.pkg;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.pkg.vo.*;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageRespVO;
import com.yanzu.module.member.service.pkg.PkgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.concurrent.TimeUnit;

import static com.yanzu.framework.common.pojo.CommonResult.success;

@Tag(name = "miniapp - 套餐")
@RestController
@RequestMapping("/member/pkg")
@Validated
@Slf4j
public class PkgController {

    @Resource
    private PkgService pkgService;

    @PostMapping("/admin/getAdminPkgPage")
    @Operation(summary = "管理员获取套餐分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppAdminPkgPageRespVO>> getAdminPkgPage(@RequestBody @Valid AppAdminPkgPageReqVO reqVO) {
        return success(pkgService.getAdminPkgPage(reqVO));
    }


    @PostMapping("/admin/saveAdminPkg")
    @Operation(summary = "管理员保存套餐信息")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> saveAdminPkg(@RequestBody @Valid AppAdminPkgSaveReqVO reqVO) {
        pkgService.saveAdminPkg(reqVO);
        return success(true);
    }


    @PostMapping("/admin/enable/{pkgId}")
    @Operation(summary = "管理员启用/禁用套餐")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> enable(@PathVariable("pkgId")Long pkgId) {
        pkgService.enable(pkgId);
        return success(true);
    }

    @PostMapping("/admin/delete/{pkgId}")
    @Operation(summary = "管理员删除套餐")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> delete(@PathVariable("pkgId")Long pkgId) {
        pkgService.delete(pkgId);
        return success(true);
    }

    @PostMapping("/getPkgPage")
    @Operation(summary = "获取套餐分页(下单页面和购买套餐时调用)")
//    @PreAuthenticated
    public CommonResult<PageResult<AppPkgPageRespVO>> getPkgPage(@RequestBody @Valid AppPkgPageReqVO reqVO) {
        return success(pkgService.getPkgPage(reqVO));
    }

    @PostMapping("/getMyPkgPage")
    @Operation(summary = "获取套餐分页(获取我的套餐时调用)")
    @PreAuthenticated
    public CommonResult<PageResult<AppPkgMyPageRespVO>> getMyPkgPage(@RequestBody @Valid AppMyPkgPageReqVO reqVO) {
        return success(pkgService.getMyPkgPage(reqVO));
    }

    @PostMapping("/preBuyPkg/{pkgId}")
    @Operation(summary = "购买套餐预下单（获取支付参数）")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<WxPayOrderRespVO> preBuyPkg(@PathVariable("pkgId")Long pkgId) {
        return success(pkgService.preBuyPkg(pkgId));
    }


}
