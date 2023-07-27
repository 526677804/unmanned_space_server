package com.yanzu.module.member.controller.app.chart;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.chart.vo.AppBusinessStatisticsRespVO;
import com.yanzu.module.member.controller.app.chart.vo.AppChartDataReqVO;
import com.yanzu.module.member.controller.app.chart.vo.AppRevenueChartRespVO;
import com.yanzu.module.member.controller.app.game.vo.AppGameInfoReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

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

    @PostMapping("/getRevenueChart")
    @Operation(summary = "获取营业额数据 （收入、提现、待提现）")
    @PreAuthenticated
    public CommonResult<AppRevenueChartRespVO> getRevenueChart(@RequestParam("storeId") Long storeId) {
        return null;
    }


    @PostMapping("/getBusinessStatistics")
    @Operation(summary = "获取经营统计数据（累积收入、订单数、新增会员）")
    @PreAuthenticated
    public CommonResult<AppBusinessStatisticsRespVO> getBusinessStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return null;
    }

    @PostMapping("/getRevenueStatistics")
    @Operation(summary = "获取收入统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, BigDecimal>>> getRevenueStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return null;
    }

    @PostMapping("/getOrderStatistics")
    @Operation(summary = "获取订单统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, Integer>>> getOrderStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return null;
    }


    @PostMapping("/getMemberStatistics")
    @Operation(summary = "获取消费人数统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, Integer>>> getMemberStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return null;
    }


    @PostMapping("/getNewMemberStatistics")
    @Operation(summary = "获取新增会员数量统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, Integer>>> getNewMemberStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return null;
    }


    @PostMapping("/getPayStatistics")
    @Operation(summary = "获取消费统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, BigDecimal>>> getPayStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return null;
    }

    @PostMapping("/getRoomUseStatistics")
    @Operation(summary = "获取房间使用率统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, BigDecimal>>> getRoomUseStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return null;
    }

    @PostMapping("/getRoomUseHourStatistics")
    @Operation(summary = "获取房间使用时长统计")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, Double>>> getRoomUseHourStatistics(@RequestBody AppChartDataReqVO reqVO) {
        return null;
    }
}
