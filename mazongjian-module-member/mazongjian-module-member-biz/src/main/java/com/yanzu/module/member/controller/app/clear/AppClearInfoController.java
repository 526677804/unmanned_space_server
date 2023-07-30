package com.yanzu.module.member.controller.app.clear;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.clear.vo.*;
import com.yanzu.module.member.service.clear.AppClearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yanzu.framework.common.pojo.CommonResult.success;

@Tag(name = "miniapp - 保洁角色使用")
@RestController
@RequestMapping("/member/clear")
@Validated
@Slf4j
public class AppClearInfoController {

    @Resource
    private AppClearService appClearService;


    @PostMapping("/getClearPage")
    @Operation(summary = "获取任务大厅分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppClearPageRespVO>> getClearPage(@RequestBody @Valid AppClearPageReqVO reqVO) {
        return success(appClearService.getClearPage(reqVO));
    }

    @PutMapping("/jiedan/{id}")
    @Operation(summary = "接单")
    @PreAuthenticated
    public CommonResult<Boolean> jiedan(@PathVariable("id") Long id) {
        appClearService.changeStatus(id, 1);
        return success(true);
    }

    @PutMapping("/start/{id}")
    @Operation(summary = "开始")
    @PreAuthenticated
    public CommonResult<Boolean> start(@PathVariable("id") Long id) {
        appClearService.changeStatus(id, 2);
        return success(true);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消")
    @PreAuthenticated
    public CommonResult<Boolean> cancel(@PathVariable("id") Long id) {
        appClearService.changeStatus(id, 3);
        return success(true);
    }

    @PostMapping("/finish/{id}")
    @Operation(summary = "完成")
    @PreAuthenticated
    public CommonResult<Boolean> finish(@RequestBody @Valid AppStartClearReqVO reqVO) {
        appClearService.finish(reqVO);
        return success(true);
    }

    @PutMapping("/changeStatus/{id}/{status}")
    @Operation(summary = "修改任务状态（接单1/开始2/取消3/完成4）")
    @PreAuthenticated
    public CommonResult<Boolean> changeStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        appClearService.changeStatus(id, status);
        return success(true);
    }

    @PutMapping("/openStoreDoor/{id}")
    @Operation(summary = "任务大厅-(开关)门店的大门,传任务id", description = "保洁任务大厅使用")
    @PreAuthenticated
    public CommonResult<Boolean> openStoreDoor(@PathVariable("id") Long id) {
        appClearService.openStoreDoor(id);
        return success(true);
    }

    @PutMapping("/openRoomDoor/{id}")
    @Operation(summary = "任务大厅-(开关)房间的大门,传任务id", description = "保洁任务大厅使用")
    @PreAuthenticated
    public CommonResult<Boolean> openRoomDoor(@PathVariable("id") Long id) {
        appClearService.openRoomDoor(id);
        return success(true);
    }


    @GetMapping("/getDetail/{id}")
    @Operation(summary = "获取保洁任务详情")
    @PreAuthenticated
    public CommonResult<AppClearInfoRespVO> getDetail(@PathVariable("id") Long id) {
        return success(appClearService.getDetail(id));
    }

    @GetMapping("/getChartData")
    @Operation(summary = "获取统计图表数据")
    @PreAuthenticated
    public CommonResult<AppClearChartRespVO> getChartData() {
        return success(appClearService.getChartData());
    }

    @PostMapping("/getClearBillPage")
    @Operation(summary = "获取结算账单记录分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppClearBillRespVO>> getClearBillPage(@RequestBody @Valid AppClearBillReqVO reqVO) {
        return success(appClearService.getClearBillPage(reqVO));
    }

}
