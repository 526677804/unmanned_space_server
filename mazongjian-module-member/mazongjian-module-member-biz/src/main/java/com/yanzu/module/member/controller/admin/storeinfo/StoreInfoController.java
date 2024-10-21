package com.yanzu.module.member.controller.admin.storeinfo;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.storeinfo.vo.*;
import com.yanzu.module.member.convert.storeinfo.StoreInfoConvert;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yanzu.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 门店管理")
@RestController
@RequestMapping("/member/store-info")
@Validated
public class StoreInfoController {

    @Resource
    private StoreInfoService storeInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建门店管理")
    @PreAuthorize("@ss.hasPermission('member:store-info:create')")
    public CommonResult<Long> createStoreInfo(@Valid @RequestBody StoreInfoCreateReqVO createReqVO) {
        return success(storeInfoService.createStoreInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新门店管理")
    @PreAuthorize("@ss.hasPermission('member:store-info:update')")
    public CommonResult<Boolean> updateStoreInfo(@Valid @RequestBody StoreInfoUpdateReqVO updateReqVO) {
        storeInfoService.updateStoreInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除门店管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:store-info:delete')")
    public CommonResult<Boolean> deleteStoreInfo(@RequestParam("id") Long id) {
        storeInfoService.deleteStoreInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得门店管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:store-info:query')")
    public CommonResult<StoreInfoRespVO> getStoreInfo(@RequestParam("id") Long id) {
        return success(storeInfoService.getStoreInfo(id));
    }

    @GetMapping("/meituanScope/{storeId}")
    @Operation(summary = "美团授权链接")
    @PreAuthorize("@ss.hasPermission('member:device-info:create')")
    public CommonResult<String> meituanScope(@PathVariable("storeId")Long storeId) {
        return success(storeInfoService.meituanScope(storeId));
    }

//    @GetMapping("/list")
//    @Operation(summary = "获得门店管理列表")
//    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
//    @PreAuthorize("@ss.hasPermission('member:store-info:query')")
//    public CommonResult<List<StoreInfoRespVO>> getStoreInfoList(@RequestParam("ids") Collection<Long> ids) {
//        List<StoreInfoDO> list = storeInfoService.getStoreInfoList(ids);
//        return success(StoreInfoConvert.INSTANCE.convertList(list));
//    }

    @GetMapping("/page")
    @Operation(summary = "获得门店管理分页")
    @PreAuthorize("@ss.hasPermission('member:store-info:query')")
    public CommonResult<PageResult<StoreInfoRespVO>> getStoreInfoPage(@Valid StoreInfoPageReqVO pageVO) {
        PageResult<StoreInfoDO> pageResult = storeInfoService.getStoreInfoPage(pageVO);
        return success(StoreInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/renew")
    @Operation(summary = "续费门店")
    @PreAuthorize("@ss.hasPermission('member:store-info:update')")
    public CommonResult<Boolean> renew(@Valid @RequestBody StoreRenewReqVO reqVO) {
        storeInfoService.renew(reqVO);
        return success(true);
    }

//    @GetMapping("/export-excel")
//    @Operation(summary = "导出门店管理 Excel")
//    @PreAuthorize("@ss.hasPermission('member:store-info:export')")
//    @OperateLog(type = EXPORT)
//    public void exportStoreInfoExcel(@Valid StoreInfoExportReqVO exportReqVO,
//              HttpServletResponse response) throws IOException {
//        List<StoreInfoDO> list = storeInfoService.getStoreInfoList(exportReqVO);
//        // 导出 Excel
//        List<StoreInfoExcelVO> datas = StoreInfoConvert.INSTANCE.convertList02(list);
//        ExcelUtils.write(response, "门店管理.xls", "数据", StoreInfoExcelVO.class, datas);
//    }

}
