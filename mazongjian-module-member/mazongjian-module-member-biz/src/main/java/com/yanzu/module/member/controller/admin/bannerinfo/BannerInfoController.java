package com.yanzu.module.member.controller.admin.bannerinfo;

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

import com.yanzu.module.member.controller.admin.bannerinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.bannerinfo.BannerInfoDO;
import com.yanzu.module.member.convert.bannerinfo.BannerInfoConvert;
import com.yanzu.module.member.service.bannerinfo.BannerInfoService;

@Tag(name = "管理后台 - 广告管理")
@RestController
@RequestMapping("/member/banner-info")
@Validated
public class BannerInfoController {

    @Resource
    private BannerInfoService bannerInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建广告管理")
    @PreAuthorize("@ss.hasPermission('member:banner-info:create')")
    public CommonResult<Long> createBannerInfo(@Valid @RequestBody BannerInfoCreateReqVO createReqVO) {
        return success(bannerInfoService.createBannerInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新广告管理")
    @PreAuthorize("@ss.hasPermission('member:banner-info:update')")
    public CommonResult<Boolean> updateBannerInfo(@Valid @RequestBody BannerInfoUpdateReqVO updateReqVO) {
        bannerInfoService.updateBannerInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除广告管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:banner-info:delete')")
    public CommonResult<Boolean> deleteBannerInfo(@RequestParam("id") Long id) {
        bannerInfoService.deleteBannerInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得广告管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:banner-info:query')")
    public CommonResult<BannerInfoRespVO> getBannerInfo(@RequestParam("id") Long id) {
        BannerInfoDO bannerInfo = bannerInfoService.getBannerInfo(id);
        return success(BannerInfoConvert.INSTANCE.convert(bannerInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得广告管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:banner-info:query')")
    public CommonResult<List<BannerInfoRespVO>> getBannerInfoList(@RequestParam("ids") Collection<Long> ids) {
        List<BannerInfoDO> list = bannerInfoService.getBannerInfoList(ids);
        return success(BannerInfoConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得广告管理分页")
    @PreAuthorize("@ss.hasPermission('member:banner-info:query')")
    public CommonResult<PageResult<BannerInfoRespVO>> getBannerInfoPage(@Valid BannerInfoPageReqVO pageVO) {
        PageResult<BannerInfoDO> pageResult = bannerInfoService.getBannerInfoPage(pageVO);
        return success(BannerInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出广告管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:banner-info:export')")
    @OperateLog(type = EXPORT)
    public void exportBannerInfoExcel(@Valid BannerInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<BannerInfoDO> list = bannerInfoService.getBannerInfoList(exportReqVO);
        // 导出 Excel
        List<BannerInfoExcelVO> datas = BannerInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "广告管理.xls", "数据", BannerInfoExcelVO.class, datas);
    }

}
