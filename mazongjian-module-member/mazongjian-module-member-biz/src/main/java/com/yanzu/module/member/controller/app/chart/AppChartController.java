package com.yanzu.module.member.controller.app.chart;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.chart.vo.*;
import com.yanzu.module.member.service.manager.AppMangerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

import static com.yanzu.framework.common.pojo.CommonResult.success;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.chart
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/27 10:27
 */
@Tag(name = "miniapp - 管理员 经营统计")
@RestController
@RequestMapping("/member/chart")
@Validated
@Slf4j
public class AppChartController {

    @Resource
    private AppMangerService appMangerService;

    @PostMapping("/getRevenueChart")
    @Operation(summary = "获取营业额数据 （总收入、总订单数）")
    @PreAuthenticated
    public CommonResult<AppRevenueChartRespVO> getRevenueChart(@RequestBody AppChartDataReqVO reqVO) {
        return success(appMangerService.getRevenueChart(reqVO));
    }


    @PostMapping("/getBusinessStatistics")
    @Operation(summary = "获取经营统计数据（微信，团购收入、订单数、下单人数）")
    @PreAuthenticated
    public CommonResult<AppBusinessStatisticsRespVO> getBusinessStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return success(appMangerService.getBusinessStatistics(reqVO));
    }

    @PostMapping("/getRevenueStatistics")
    @Operation(summary = "获取收入统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, BigDecimal>>> getRevenueStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return success(appMangerService.getRevenueStatistics(reqVO));
    }

    @PostMapping("/getOrderStatistics")
    @Operation(summary = "获取订单统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, Integer>>> getOrderStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return success(appMangerService.getOrderStatistics(reqVO));
    }


    @PostMapping("/getMemberStatistics")
    @Operation(summary = "获取消费人数统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, Integer>>> getMemberStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return success(appMangerService.getMemberStatistics(reqVO));
    }


//    @PostMapping("/getNewMemberStatistics")
//    @Operation(summary = "获取新增会员数量统计")
//    @PreAuthenticated
//    public CommonResult<List<KeyValue<String, Integer>>> getNewMemberStatistics(@RequestBody AppChartDataReqVO reqVO) {
//        return success(appMangerService.getNewMemberStatistics(reqVO));
//    }


//    @PostMapping("/getRechargeStatistics")
//    @Operation(summary = "获取充值统计")
//    @PreAuthenticated
//    public CommonResult<List<KeyValue<String, BigDecimal>>> getRechargeStatistics(@RequestBody AppChartDataReqVO reqVO) {
//        return success(appMangerService.getRechargeStatistics(reqVO));
//    }

    @PostMapping("/getRoomUseStatistics")
    @Operation(summary = "获取房间使用率统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, String>>> getRoomUseStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return success(appMangerService.getRoomUseStatistics(reqVO));
    }

    @PostMapping("/getRoomUseHourStatistics")
    @Operation(summary = "获取房间使用时长统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, String>>> getRoomUseHourStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return success(appMangerService.getRoomUseHourStatistics(reqVO));
    }

    @PostMapping("/getIncomeStatistics")
    @Operation(summary = "获取收入明细")
    @PreAuthenticated
    public CommonResult<List<AppIncomeStatisticsRespVO>> getIncomeStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return success(appMangerService.getIncomeStatistics(reqVO));
    }

    @PostMapping("/getRechargeStatistics")
    @Operation(summary = "获取充值明细")
    @PreAuthenticated
    public CommonResult<List<AppRechargeStatisticsRespVO>> getRechargeStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return success(appMangerService.getRechargeStatistics(reqVO));
    }

}
