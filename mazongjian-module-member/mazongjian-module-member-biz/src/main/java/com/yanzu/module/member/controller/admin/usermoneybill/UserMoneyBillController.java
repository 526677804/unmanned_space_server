package com.yanzu.module.member.controller.admin.usermoneybill;

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

import com.yanzu.module.member.controller.admin.usermoneybill.vo.*;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import com.yanzu.module.member.convert.usermoneybill.UserMoneyBillConvert;
import com.yanzu.module.member.service.usermoneybill.UserMoneyBillService;

@Tag(name = "管理后台 - 用户账单明细")
@RestController
@RequestMapping("/member/user-money-bill")
@Validated
public class UserMoneyBillController {

    @Resource
    private UserMoneyBillService userMoneyBillService;

    @GetMapping("/get")
    @Operation(summary = "获得用户账单明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:user-money-bill:query')")
    public CommonResult<UserMoneyBillRespVO> getUserMoneyBill(@RequestParam("id") Long id) {
        UserMoneyBillDO userMoneyBill = userMoneyBillService.getUserMoneyBill(id);
        return success(UserMoneyBillConvert.INSTANCE.convert(userMoneyBill));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户账单明细分页")
    @PreAuthorize("@ss.hasPermission('member:user-money-bill:query')")
    public CommonResult<PageResult<UserMoneyBillRespVO>> getUserMoneyBillPage(@Valid UserMoneyBillPageReqVO pageVO) {
        PageResult<UserMoneyBillDO> pageResult = userMoneyBillService.getUserMoneyBillPage(pageVO);
        return success(UserMoneyBillConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出用户账单明细 Excel")
    @PreAuthorize("@ss.hasPermission('member:user-money-bill:export')")
    @OperateLog(type = EXPORT)
    public void exportUserMoneyBillExcel(@Valid UserMoneyBillExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<UserMoneyBillDO> list = userMoneyBillService.getUserMoneyBillList(exportReqVO);
        // 导出 Excel
        List<UserMoneyBillExcelVO> datas = UserMoneyBillConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "用户账单明细.xls", "数据", UserMoneyBillExcelVO.class, datas);
    }

}
