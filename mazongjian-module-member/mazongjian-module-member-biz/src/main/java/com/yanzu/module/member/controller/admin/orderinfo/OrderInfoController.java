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
import com.yanzu.module.member.service.orderinfo.OrderInfoService;

@Tag(name = "管理后台 - 订单管理")
@RestController
@RequestMapping("/member/order-info")
@Validated
public class OrderInfoController {

    @Resource
    private OrderInfoService orderInfoService;


    @DeleteMapping("/delete")
    @Operation(summary = "删除订单管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:order-info:delete')")
    public CommonResult<Boolean> deleteOrderInfo(@RequestParam("id") Long id) {
        orderInfoService.deleteOrderInfo(id);
        return success(true);
    }


    @GetMapping("/page")
    @Operation(summary = "获得订单管理分页")
    @PreAuthorize("@ss.hasPermission('member:order-info:query')")
    public CommonResult<PageResult<OrderInfoRespVO>> getOrderInfoPage(@Valid OrderInfoPageReqVO pageVO) {
        return success(orderInfoService.getOrderInfoPage(pageVO));
    }


}
