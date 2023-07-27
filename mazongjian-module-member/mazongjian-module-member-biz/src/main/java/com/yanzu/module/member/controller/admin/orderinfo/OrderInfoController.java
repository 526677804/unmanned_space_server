package com.yanzu.module.member.controller.admin.orderinfo;

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

import com.yanzu.module.member.controller.admin.orderinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.convert.orderinfo.OrderInfoConvert;
import com.yanzu.module.member.service.orderinfo.OrderInfoService;

@Tag(name = "管理后台 - 订单管理")
@RestController
@RequestMapping("/member/order-info")
@Validated
public class OrderInfoController {

    @Resource
    private OrderInfoService orderInfoService;



    @GetMapping("/get")
    @Operation(summary = "获得订单管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:order-info:query')")
    public CommonResult<OrderInfoRespVO> getOrderInfo(@RequestParam("id") Long id) {
        OrderInfoDO orderInfo = orderInfoService.getOrderInfo(id);
        return success(OrderInfoConvert.INSTANCE.convert(orderInfo));
    }


    @GetMapping("/page")
    @Operation(summary = "获得订单管理分页")
    @PreAuthorize("@ss.hasPermission('member:order-info:query')")
    public CommonResult<PageResult<OrderInfoRespVO>> getOrderInfoPage(@Valid OrderInfoPageReqVO pageVO) {
        PageResult<OrderInfoDO> pageResult = orderInfoService.getOrderInfoPage(pageVO);
        return success(OrderInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出订单管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:order-info:export')")
    @OperateLog(type = EXPORT)
    public void exportOrderInfoExcel(@Valid OrderInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<OrderInfoDO> list = orderInfoService.getOrderInfoList(exportReqVO);
        // 导出 Excel
        List<OrderInfoExcelVO> datas = OrderInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "订单管理.xls", "数据", OrderInfoExcelVO.class, datas);
    }

}
