package com.yanzu.module.member.controller.admin.user;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.excel.core.util.ExcelUtils;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import com.yanzu.module.member.controller.admin.user.vo.*;
import com.yanzu.module.member.convert.user.AppUserConvert;
import com.yanzu.module.member.dal.dataobject.user.AppUserDO;
import com.yanzu.module.member.service.user.MemberUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yanzu.framework.common.pojo.CommonResult.success;
import static com.yanzu.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 用户管理")
@RestController
@RequestMapping("/member/app-user")
@Validated
public class MemberUserController {

    @Resource
    private MemberUserService memberUserService;

    @PostMapping("/create")
    @Operation(summary = "添加用户管理")
    @PreAuthorize("@ss.hasPermission('member:app-user:create')")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Long> createAppUser(@Valid @RequestBody AppUserCreateReqVO createReqVO) {
        createReqVO.setMobile(createReqVO.getMobile().trim());
        return success(memberUserService.createAppUser(createReqVO));
    }

    @PostMapping("/recharge")
    @Operation(summary = "用户余额充值")
    @PreAuthorize("@ss.hasPermission('member:app-user:update')")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> recharge(@Valid @RequestBody AppUserRechargeReqVO reqVO) {
        memberUserService.recharge(reqVO);
        return success(true);
    }


    @PutMapping("/update")
    @Operation(summary = "更新用户管理")
    @PreAuthorize("@ss.hasPermission('member:app-user:update')")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> updateAppUser(@Valid @RequestBody AppUserUpdateReqVO updateReqVO) {
        memberUserService.updateAppUser(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:app-user:delete')")
    public CommonResult<Boolean> deleteAppUser(@RequestParam("id") Long id) {
        memberUserService.deleteAppUser(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:app-user:query')")
    public CommonResult<AppUserRespVO> getAppUser(@RequestParam("id") Long id) {
        AppUserDO appUser = memberUserService.getAppUser(id);
        return success(AppUserConvert.INSTANCE.convert(appUser));
    }

    @GetMapping("/list")
    @Operation(summary = "获得用户管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:app-user:query')")
    public CommonResult<List<AppUserRespVO>> getAppUserList(@RequestParam("ids") Collection<Long> ids) {
        List<AppUserDO> list = memberUserService.getAppUserList(ids);
        return success(AppUserConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户管理分页")
    @PreAuthorize("@ss.hasPermission('member:app-user:query')")
    public CommonResult<PageResult<AppUserRespVO>> getAppUserPage(@Valid AppUserPageReqVO pageVO) {
        PageResult<AppUserDO> pageResult = memberUserService.getAppUserPage(pageVO);
        return success(AppUserConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出用户管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:app-user:export')")
    @OperateLog(type = EXPORT)
    public void exportAppUserExcel(@Valid AppUserExportReqVO exportReqVO,
                                   HttpServletResponse response) throws IOException {
        List<AppUserDO> list = memberUserService.getAppUserList(exportReqVO);
        // 导出 Excel
        List<AppUserExcelVO> datas = AppUserConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "用户管理.xls", "数据", AppUserExcelVO.class, datas);
    }

}
