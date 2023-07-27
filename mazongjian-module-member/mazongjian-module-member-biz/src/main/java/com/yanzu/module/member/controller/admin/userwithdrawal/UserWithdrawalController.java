package com.yanzu.module.member.controller.admin.userwithdrawal;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.common.pojo.CommonResult;
import static com.yanzu.framework.common.pojo.CommonResult.success;

import com.yanzu.framework.excel.core.util.ExcelUtils;

import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import static com.yanzu.framework.operatelog.core.enums.OperateTypeEnum.*;

import com.yanzu.module.member.controller.admin.userwithdrawal.vo.*;
import com.yanzu.module.member.dal.dataobject.userwithdrawal.UserWithdrawalDO;
import com.yanzu.module.member.convert.userwithdrawal.UserWithdrawalConvert;
import com.yanzu.module.member.service.userwithdrawal.UserWithdrawalService;

@Tag(name = "管理后台 - 用户提现")
@RestController
@RequestMapping("/member/user-withdrawal")
@Validated
public class UserWithdrawalController {

    @Resource
    private UserWithdrawalService userWithdrawalService;

    @GetMapping("/get")
    @Operation(summary = "获得用户提现")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:user-withdrawal:query')")
    public CommonResult<UserWithdrawalRespVO> getUserWithdrawal(@RequestParam("id") Long id) {
        UserWithdrawalDO userWithdrawal = userWithdrawalService.getUserWithdrawal(id);
        return success(UserWithdrawalConvert.INSTANCE.convert(userWithdrawal));
    }

    @GetMapping("/list")
    @Operation(summary = "获得用户提现列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:user-withdrawal:query')")
    public CommonResult<List<UserWithdrawalRespVO>> getUserWithdrawalList(@RequestParam("ids") Collection<Long> ids) {
        List<UserWithdrawalDO> list = userWithdrawalService.getUserWithdrawalList(ids);
        return success(UserWithdrawalConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户提现分页")
    @PreAuthorize("@ss.hasPermission('member:user-withdrawal:query')")
    public CommonResult<PageResult<UserWithdrawalRespVO>> getUserWithdrawalPage(@Valid UserWithdrawalPageReqVO pageVO) {
        PageResult<UserWithdrawalDO> pageResult = userWithdrawalService.getUserWithdrawalPage(pageVO);
        return success(UserWithdrawalConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出用户提现 Excel")
    @PreAuthorize("@ss.hasPermission('member:user-withdrawal:export')")
    @OperateLog(type = EXPORT)
    public void exportUserWithdrawalExcel(@Valid UserWithdrawalExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<UserWithdrawalDO> list = userWithdrawalService.getUserWithdrawalList(exportReqVO);
        // 导出 Excel
        List<UserWithdrawalExcelVO> datas = UserWithdrawalConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "用户提现.xls", "数据", UserWithdrawalExcelVO.class, datas);
    }

}
