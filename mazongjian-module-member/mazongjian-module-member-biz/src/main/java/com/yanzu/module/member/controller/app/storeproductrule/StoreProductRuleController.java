package com.yanzu.module.member.controller.app.storeproductrule;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.excel.core.util.ExcelUtils;
import com.yanzu.module.member.controller.app.storeproductrule.vo.*;
import com.yanzu.module.member.convert.storeproductrule.StoreProductRuleConvert;
import com.yanzu.module.member.dal.dataobject.storeproductrule.StoreProductRuleDO;
import com.yanzu.module.member.service.storeproductrule.StoreProductRuleService;
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


@Tag(name = "管理后台 - 商品规则值(规格)")
@RestController
@RequestMapping("/product/store-product-rule")
@Validated
public class StoreProductRuleController {

    @Resource
    private StoreProductRuleService storeProductRuleService;


    @PostMapping("/save/{id}")
    @Operation(summary = "创建与更新商品规则值(规格)")
    @PreAuthorize("@ss.hasPermission('shop:store-product-rule:create')")
    public CommonResult<Integer> createStoreProductRule(@Valid @RequestBody StoreProductRuleCreateReqVO createReqVO, @PathVariable Integer id) {
        if(id != null && id > 0){
            StoreProductRuleUpdateReqVO updateReqVO = new StoreProductRuleUpdateReqVO();
            updateReqVO.setId(id);
            updateReqVO.setRuleName(createReqVO.getRuleName());
            updateReqVO.setRuleValue(createReqVO.getRuleValue());
            storeProductRuleService.updateStoreProductRule(updateReqVO);
            return success(1);
        }else{
           return success(storeProductRuleService.createStoreProductRule(createReqVO));
        }
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除商品规则值(规格)")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('shop:store-product-rule:delete')")
    public CommonResult<Boolean> deleteStoreProductRule(@RequestParam("id") Integer id) {
        storeProductRuleService.deleteStoreProductRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得商品规则值(规格)")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('shop:store-product-rule:query')")
    public CommonResult<StoreProductRuleRespVO> getStoreProductRule(@RequestParam("id") Integer id) {
        StoreProductRuleDO storeProductRule = storeProductRuleService.getStoreProductRule(id);
        return success(StoreProductRuleConvert.INSTANCE.convert(storeProductRule));
    }

    @GetMapping("/list")
    @Operation(summary = "获得商品规则值(规格)列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('shop:store-product-rule:query')")
    public CommonResult<List<StoreProductRuleRespVO>> getStoreProductRuleList(@RequestParam("ids") Collection<Integer> ids) {
        List<StoreProductRuleDO> list = storeProductRuleService.getStoreProductRuleList(ids);
        return success(StoreProductRuleConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品规则值(规格)分页")
    @PreAuthorize("@ss.hasPermission('shop:store-product-rule:query')")
    public CommonResult<PageResult<StoreProductRuleRespVO>> getStoreProductRulePage(@Valid StoreProductRulePageReqVO pageVO) {
        PageResult<StoreProductRuleDO> pageResult = storeProductRuleService.getStoreProductRulePage(pageVO);
        System.out.println("aa:"+pageResult);
        return success(StoreProductRuleConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出商品规则值(规格) Excel")
    @PreAuthorize("@ss.hasPermission('shop:store-product-rule:export')")
    public void exportStoreProductRuleExcel(@Valid StoreProductRuleExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<StoreProductRuleDO> list = storeProductRuleService.getStoreProductRuleList(exportReqVO);
        // 导出 Excel
        List<StoreProductRuleExcelVO> datas = StoreProductRuleConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "商品规则值(规格).xls", "数据", StoreProductRuleExcelVO.class, datas);
    }

}
