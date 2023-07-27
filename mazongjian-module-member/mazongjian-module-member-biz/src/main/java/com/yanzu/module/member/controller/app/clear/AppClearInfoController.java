package com.yanzu.module.member.controller.app.clear;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.clear.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "miniapp - 保洁角色使用")
@RestController
@RequestMapping("/member/clear")
@Validated
@Slf4j
public class AppClearInfoController {

    @PostMapping("/getClearPage")
    @Operation(summary = "获取任务大厅分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppClearPageRespVO>> getClearPage(@RequestBody @Valid AppClearPageReqVO reqVO) {
        return null;
    }

    @PutMapping("/changeStatus/{id}/{status}")
    @Operation(summary = "修改任务状态（接单/开始/取消/完成）status值见字典")
    @PreAuthenticated
    public CommonResult<Boolean> changeStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        return null;
    }

    @PutMapping("/openStoreDoor/{orderId}")
    @Operation(summary = "任务大厅-(开关)门店的大门", description = "保洁任务大厅使用")
    @PreAuthenticated
    public CommonResult<Boolean> openStoreDoor(@PathVariable("orderId") Long orderId) {
        return null;
    }

    @PutMapping("/openRoomDoor/{orderId}")
    @Operation(summary = "任务大厅-(开关)房间的大门", description = "保洁任务大厅使用")
    @PreAuthenticated
    public CommonResult<Boolean> openRoomDoor(@PathVariable("orderId") Long orderId) {
        return null;
    }


    @GetMapping("/getDetail/{id}")
    @Operation(summary = "获取保洁任务详情")
    @PreAuthenticated
    public CommonResult<AppClearInfoRespVO> getDetail(@PathVariable("id") Long id) {
        return null;
    }

    @GetMapping("/getChartData")
    @Operation(summary = "获取统计图表数据")
    @PreAuthenticated
    public CommonResult<AppClearChartRespVO> getChartData() {
        return null;
    }

    @PostMapping("/getClearBillPage")
    @Operation(summary = "获取结算账单记录分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppClearBillRespVO>> getClearBillPage(@RequestBody @Valid AppClearBillReqVO reqVO) {
        return null;
    }

}
