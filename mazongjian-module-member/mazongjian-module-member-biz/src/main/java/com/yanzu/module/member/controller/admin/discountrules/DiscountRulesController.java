package com.yanzu.module.member.controller.admin.discountrules;

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

import com.yanzu.module.member.controller.admin.discountrules.vo.*;
import com.yanzu.module.member.dal.dataobject.discountrules.DiscountRulesDO;
import com.yanzu.module.member.convert.discountrules.DiscountRulesConvert;
import com.yanzu.module.member.service.discountrules.DiscountRulesService;

@Tag(name = "管理后台 - 充值优惠规则管理")
@RestController
@RequestMapping("/member/discount-rules")
@Validated
public class DiscountRulesController {

    @Resource
    private DiscountRulesService discountRulesService;

    @PostMapping("/create")
    @Operation(summary = "创建充值优惠规则管理")
    @PreAuthorize("@ss.hasPermission('member:discount-rules:create')")
    public CommonResult<Long> createDiscountRules(@Valid @RequestBody DiscountRulesCreateReqVO createReqVO) {
        return success(discountRulesService.createDiscountRules(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新充值优惠规则管理")
    @PreAuthorize("@ss.hasPermission('member:discount-rules:update')")
    public CommonResult<Boolean> updateDiscountRules(@Valid @RequestBody DiscountRulesUpdateReqVO updateReqVO) {
        discountRulesService.updateDiscountRules(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除充值优惠规则管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:discount-rules:delete')")
    public CommonResult<Boolean> deleteDiscountRules(@RequestParam("id") Long id) {
        discountRulesService.deleteDiscountRules(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得充值优惠规则管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:discount-rules:query')")
    public CommonResult<DiscountRulesRespVO> getDiscountRules(@RequestParam("id") Long id) {
        DiscountRulesDO discountRules = discountRulesService.getDiscountRules(id);
        return success(DiscountRulesConvert.INSTANCE.convert(discountRules));
    }

    @GetMapping("/list")
    @Operation(summary = "获得充值优惠规则管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:discount-rules:query')")
    public CommonResult<List<DiscountRulesRespVO>> getDiscountRulesList(@RequestParam("ids") Collection<Long> ids) {
        List<DiscountRulesDO> list = discountRulesService.getDiscountRulesList(ids);
        return success(DiscountRulesConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得充值优惠规则管理分页")
    @PreAuthorize("@ss.hasPermission('member:discount-rules:query')")
    public CommonResult<PageResult<DiscountRulesRespVO>> getDiscountRulesPage(@Valid DiscountRulesPageReqVO pageVO) {
        PageResult<DiscountRulesDO> pageResult = discountRulesService.getDiscountRulesPage(pageVO);
        return success(DiscountRulesConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出充值优惠规则管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:discount-rules:export')")
    @OperateLog(type = EXPORT)
    public void exportDiscountRulesExcel(@Valid DiscountRulesExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<DiscountRulesDO> list = discountRulesService.getDiscountRulesList(exportReqVO);
        // 导出 Excel
        List<DiscountRulesExcelVO> datas = DiscountRulesConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "充值优惠规则管理.xls", "数据", DiscountRulesExcelVO.class, datas);
    }

}
