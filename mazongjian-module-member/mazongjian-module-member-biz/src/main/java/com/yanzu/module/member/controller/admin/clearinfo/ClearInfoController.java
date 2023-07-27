package com.yanzu.module.member.controller.admin.clearinfo;

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

import com.yanzu.module.member.controller.admin.clearinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.convert.clearinfo.ClearInfoConvert;
import com.yanzu.module.member.service.clearinfo.ClearInfoService;

@Tag(name = "管理后台 - 保洁信息管理")
@RestController
@RequestMapping("/member/clear-info")
@Validated
public class ClearInfoController {

    @Resource
    private ClearInfoService clearInfoService;

    @PutMapping("/update")
    @Operation(summary = "更新保洁信息管理")
    @PreAuthorize("@ss.hasPermission('member:clear-info:update')")
    public CommonResult<Boolean> updateClearInfo(@Valid @RequestBody ClearInfoUpdateReqVO updateReqVO) {
        clearInfoService.updateClearInfo(updateReqVO);
        return success(true);
    }


    @GetMapping("/get")
    @Operation(summary = "获得保洁信息管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:clear-info:query')")
    public CommonResult<ClearInfoRespVO> getClearInfo(@RequestParam("id") Long id) {
        ClearInfoDO clearInfo = clearInfoService.getClearInfo(id);
        return success(ClearInfoConvert.INSTANCE.convert(clearInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得保洁信息管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:clear-info:query')")
    public CommonResult<List<ClearInfoRespVO>> getClearInfoList(@RequestParam("ids") Collection<Long> ids) {
        List<ClearInfoDO> list = clearInfoService.getClearInfoList(ids);
        return success(ClearInfoConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得保洁信息管理分页")
    @PreAuthorize("@ss.hasPermission('member:clear-info:query')")
    public CommonResult<PageResult<ClearInfoRespVO>> getClearInfoPage(@Valid ClearInfoPageReqVO pageVO) {
        PageResult<ClearInfoDO> pageResult = clearInfoService.getClearInfoPage(pageVO);
        return success(ClearInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出保洁信息管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:clear-info:export')")
    @OperateLog(type = EXPORT)
    public void exportClearInfoExcel(@Valid ClearInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ClearInfoDO> list = clearInfoService.getClearInfoList(exportReqVO);
        // 导出 Excel
        List<ClearInfoExcelVO> datas = ClearInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "保洁信息管理.xls", "数据", ClearInfoExcelVO.class, datas);
    }

}
