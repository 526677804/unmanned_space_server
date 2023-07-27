package com.yanzu.module.member.controller.admin.franchiseinfo;

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

import com.yanzu.module.member.controller.admin.franchiseinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;
import com.yanzu.module.member.convert.franchiseinfo.FranchiseInfoConvert;
import com.yanzu.module.member.service.franchiseinfo.FranchiseInfoService;

@Tag(name = "管理后台 - 加盟信息")
@RestController
@RequestMapping("/member/franchise-info")
@Validated
public class FranchiseInfoController {

    @Resource
    private FranchiseInfoService franchiseInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建加盟信息")
    @PreAuthorize("@ss.hasPermission('member:franchise-info:create')")
    public CommonResult<Long> createFranchiseInfo(@Valid @RequestBody FranchiseInfoCreateReqVO createReqVO) {
        return success(franchiseInfoService.createFranchiseInfo(createReqVO));
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除加盟信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:franchise-info:delete')")
    public CommonResult<Boolean> deleteFranchiseInfo(@RequestParam("id") Long id) {
        franchiseInfoService.deleteFranchiseInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得加盟信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:franchise-info:query')")
    public CommonResult<FranchiseInfoRespVO> getFranchiseInfo(@RequestParam("id") Long id) {
        FranchiseInfoDO franchiseInfo = franchiseInfoService.getFranchiseInfo(id);
        return success(FranchiseInfoConvert.INSTANCE.convert(franchiseInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得加盟信息列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:franchise-info:query')")
    public CommonResult<List<FranchiseInfoRespVO>> getFranchiseInfoList(@RequestParam("ids") Collection<Long> ids) {
        List<FranchiseInfoDO> list = franchiseInfoService.getFranchiseInfoList(ids);
        return success(FranchiseInfoConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得加盟信息分页")
    @PreAuthorize("@ss.hasPermission('member:franchise-info:query')")
    public CommonResult<PageResult<FranchiseInfoRespVO>> getFranchiseInfoPage(@Valid FranchiseInfoPageReqVO pageVO) {
        PageResult<FranchiseInfoDO> pageResult = franchiseInfoService.getFranchiseInfoPage(pageVO);
        return success(FranchiseInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出加盟信息 Excel")
    @PreAuthorize("@ss.hasPermission('member:franchise-info:export')")
    @OperateLog(type = EXPORT)
    public void exportFranchiseInfoExcel(@Valid FranchiseInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<FranchiseInfoDO> list = franchiseInfoService.getFranchiseInfoList(exportReqVO);
        // 导出 Excel
        List<FranchiseInfoExcelVO> datas = FranchiseInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "加盟信息.xls", "数据", FranchiseInfoExcelVO.class, datas);
    }

}
