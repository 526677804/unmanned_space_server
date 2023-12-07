package com.yanzu.module.member.controller.admin.wxpay;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.wxpay.vo.*;
import com.yanzu.module.member.convert.member.StoreWxpayConfigConvert;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;
import com.yanzu.module.member.service.member.StoreWxpayConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.yanzu.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 门店微信支付配置")
@RestController
@RequestMapping("/member/store-wxpay-config")
@Validated
public class StoreWxpayConfigController {

    @Resource
    private StoreWxpayConfigService storeWxpayConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建门店微信支付配置")
    @PreAuthorize("@ss.hasPermission('member:store-wxpay-config:create')")
    public CommonResult<Long> createStoreWxpayConfig(@Valid @RequestBody StoreWxpayConfigCreateReqVO createReqVO) {
        return success(storeWxpayConfigService.createStoreWxpayConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新门店微信支付配置")
    @PreAuthorize("@ss.hasPermission('member:store-wxpay-config:update')")
    public CommonResult<Boolean> updateStoreWxpayConfig(@Valid @RequestBody StoreWxpayConfigUpdateReqVO updateReqVO) {
        storeWxpayConfigService.updateStoreWxpayConfig(updateReqVO);
        return success(true);
    }

    @GetMapping("/profitsharing")
    @Operation(summary = "分账授权")
    @PreAuthorize("@ss.hasPermission('member:store-wxpay-config:update')")
    public CommonResult<Boolean> profitsharing(@RequestParam("id") Long id) {
        storeWxpayConfigService.profitsharing(id);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除门店微信支付配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:store-wxpay-config:delete')")
    public CommonResult<Boolean> deleteStoreWxpayConfig(@RequestParam("id") Long id) {
        storeWxpayConfigService.deleteStoreWxpayConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得门店微信支付配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:store-wxpay-config:query')")
    public CommonResult<StoreWxpayConfigRespVO> getStoreWxpayConfig(@RequestParam("id") Long id) {
        StoreWxpayConfigDO storeWxpayConfig = storeWxpayConfigService.getStoreWxpayConfig(id);
        return success(StoreWxpayConfigConvert.INSTANCE.convert(storeWxpayConfig));
    }

    @GetMapping("/list")
    @Operation(summary = "获得门店微信支付配置列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:store-wxpay-config:query')")
    public CommonResult<List<StoreWxpayConfigRespVO>> getStoreWxpayConfigList(@RequestParam("ids") Collection<Long> ids) {
        List<StoreWxpayConfigDO> list = storeWxpayConfigService.getStoreWxpayConfigList(ids);
        return success(StoreWxpayConfigConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得门店微信支付配置分页")
    @PreAuthorize("@ss.hasPermission('member:store-wxpay-config:query')")
    public CommonResult<PageResult<StoreWxpayConfigPageRespVO>> getStoreWxpayConfigPage(@Valid StoreWxpayConfigPageReqVO pageVO) {
        return success(storeWxpayConfigService.getStoreWxpayConfigPage(pageVO));
    }

//    @GetMapping("/export-excel")
//    @Operation(summary = "导出门店微信支付配置 Excel")
//    @PreAuthorize("@ss.hasPermission('member:store-wxpay-config:export')")
//    @OperateLog(type = EXPORT)
//    public void exportStoreWxpayConfigExcel(@Valid StoreWxpayConfigExportReqVO exportReqVO,
//              HttpServletResponse response) throws IOException {
//        List<StoreWxpayConfigDO> list = storeWxpayConfigService.getStoreWxpayConfigList(exportReqVO);
//        // 导出 Excel
//        List<StoreWxpayConfigExcelVO> datas = StoreWxpayConfigConvert.INSTANCE.convertList02(list);
//        ExcelUtils.write(response, "门店微信支付配置.xls", "数据", StoreWxpayConfigExcelVO.class, datas);
//    }

}
