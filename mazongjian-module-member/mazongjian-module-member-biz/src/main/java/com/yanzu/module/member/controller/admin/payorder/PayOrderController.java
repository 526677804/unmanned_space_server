package com.yanzu.module.member.controller.admin.payorder;

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

import com.yanzu.module.member.controller.admin.payorder.vo.*;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.module.member.convert.payorder.PayOrderConvert;
import com.yanzu.module.member.service.payorder.PayOrderService;

@Tag(name = "管理后台 - 支付订单")
@RestController
@RequestMapping("/member/pay-order")
@Validated
public class PayOrderController {

    @Resource
    private PayOrderService payOrderService;


    @GetMapping("/get")
    @Operation(summary = "获得支付订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:pay-order:query')")
    public CommonResult<PayOrderRespVO> getPayOrder(@RequestParam("id") Long id) {
        PayOrderDO payOrder = payOrderService.getPayOrder(id);
        return success(PayOrderConvert.INSTANCE.convert(payOrder));
    }

    @GetMapping("/list")
    @Operation(summary = "获得支付订单列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:pay-order:query')")
    public CommonResult<List<PayOrderRespVO>> getPayOrderList(@RequestParam("ids") Collection<Long> ids) {
        List<PayOrderDO> list = payOrderService.getPayOrderList(ids);
        return success(PayOrderConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得支付订单分页")
    @PreAuthorize("@ss.hasPermission('member:pay-order:query')")
    public CommonResult<PageResult<PayOrderRespVO>> getPayOrderPage(@Valid PayOrderPageReqVO pageVO) {
        PageResult<PayOrderDO> pageResult = payOrderService.getPayOrderPage(pageVO);
        return success(PayOrderConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出支付订单 Excel")
    @PreAuthorize("@ss.hasPermission('member:pay-order:export')")
    @OperateLog(type = EXPORT)
    public void exportPayOrderExcel(@Valid PayOrderExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<PayOrderDO> list = payOrderService.getPayOrderList(exportReqVO);
        // 导出 Excel
        List<PayOrderExcelVO> datas = PayOrderConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "支付订单.xls", "数据", PayOrderExcelVO.class, datas);
    }

}
