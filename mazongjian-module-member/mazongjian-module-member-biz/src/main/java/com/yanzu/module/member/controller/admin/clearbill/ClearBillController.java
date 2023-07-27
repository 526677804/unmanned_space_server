package com.yanzu.module.member.controller.admin.clearbill;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.excel.core.util.ExcelUtils;
import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import com.yanzu.module.member.controller.admin.clearbill.vo.*;
import com.yanzu.module.member.convert.clearbill.ClearBillConvert;
import com.yanzu.module.member.dal.dataobject.clearbill.ClearBillDO;
import com.yanzu.module.member.service.clearbill.ClearBillService;
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

import static com.yanzu.framework.common.pojo.CommonResult.success;
import static com.yanzu.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 保洁账单管理")
@RestController
@RequestMapping("/member/clear-bill")
@Validated
public class ClearBillController {

    @Resource
    private ClearBillService clearBillService;


    @DeleteMapping("/delete")
    @Operation(summary = "删除保洁账单管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:clear-bill:delete')")
    public CommonResult<Boolean> deleteClearBill(@RequestParam("id") Long id) {
        clearBillService.deleteClearBill(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得保洁账单管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:clear-bill:query')")
    public CommonResult<ClearBillRespVO> getClearBill(@RequestParam("id") Long id) {
        ClearBillDO clearBill = clearBillService.getClearBill(id);
        return success(ClearBillConvert.INSTANCE.convert(clearBill));
    }


    @GetMapping("/page")
    @Operation(summary = "获得保洁账单管理分页")
    @PreAuthorize("@ss.hasPermission('member:clear-bill:query')")
    public CommonResult<PageResult<ClearBillRespVO>> getClearBillPage(@Valid ClearBillPageReqVO pageVO) {
        PageResult<ClearBillDO> pageResult = clearBillService.getClearBillPage(pageVO);
        return success(ClearBillConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出保洁账单管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:clear-bill:export')")
    @OperateLog(type = EXPORT)
    public void exportClearBillExcel(@Valid ClearBillExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ClearBillDO> list = clearBillService.getClearBillList(exportReqVO);
        // 导出 Excel
        List<ClearBillExcelVO> datas = ClearBillConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "保洁账单管理.xls", "数据", ClearBillExcelVO.class, datas);
    }

}
