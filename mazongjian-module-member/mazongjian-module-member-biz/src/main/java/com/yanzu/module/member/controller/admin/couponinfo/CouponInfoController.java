package com.yanzu.module.member.controller.admin.couponinfo;

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

import com.yanzu.module.member.controller.admin.couponinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.module.member.convert.couponinfo.CouponInfoConvert;
import com.yanzu.module.member.service.couponinfo.CouponInfoService;

@Tag(name = "管理后台 - 优惠券管理")
@RestController
@RequestMapping("/member/coupon-info")
@Validated
public class CouponInfoController {

    @Resource
    private CouponInfoService couponInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建优惠券管理")
    @PreAuthorize("@ss.hasPermission('member:coupon-info:create')")
    public CommonResult<Long> createCouponInfo(@Valid @RequestBody CouponInfoCreateReqVO createReqVO) {
        return success(couponInfoService.createCouponInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新优惠券管理")
    @PreAuthorize("@ss.hasPermission('member:coupon-info:update')")
    public CommonResult<Boolean> updateCouponInfo(@Valid @RequestBody CouponInfoUpdateReqVO updateReqVO) {
        couponInfoService.updateCouponInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除优惠券管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:coupon-info:delete')")
    public CommonResult<Boolean> deleteCouponInfo(@RequestParam("id") Long id) {
        couponInfoService.deleteCouponInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得优惠券管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:coupon-info:query')")
    public CommonResult<CouponInfoRespVO> getCouponInfo(@RequestParam("id") Long id) {
        CouponInfoDO couponInfo = couponInfoService.getCouponInfo(id);
        return success(CouponInfoConvert.INSTANCE.convert(couponInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得优惠券管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:coupon-info:query')")
    public CommonResult<List<CouponInfoRespVO>> getCouponInfoList(@RequestParam("ids") Collection<Long> ids) {
        List<CouponInfoDO> list = couponInfoService.getCouponInfoList(ids);
        return success(CouponInfoConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得优惠券管理分页")
    @PreAuthorize("@ss.hasPermission('member:coupon-info:query')")
    public CommonResult<PageResult<CouponInfoRespVO>> getCouponInfoPage(@Valid CouponInfoPageReqVO pageVO) {
        PageResult<CouponInfoDO> pageResult = couponInfoService.getCouponInfoPage(pageVO);
        return success(CouponInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出优惠券管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:coupon-info:export')")
    @OperateLog(type = EXPORT)
    public void exportCouponInfoExcel(@Valid CouponInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<CouponInfoDO> list = couponInfoService.getCouponInfoList(exportReqVO);
        // 导出 Excel
        List<CouponInfoExcelVO> datas = CouponInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "优惠券管理.xls", "数据", CouponInfoExcelVO.class, datas);
    }

}
