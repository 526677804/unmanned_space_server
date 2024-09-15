package com.yanzu.module.member.controller.admin.deviceinfo;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.excel.core.util.ExcelUtils;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;
import com.yanzu.module.member.convert.deviceinfo.DeviceInfoConvert;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.service.deviceinfo.DeviceInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yanzu.framework.common.pojo.CommonResult.success;
import static com.yanzu.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 设备管理")
@RestController
@RequestMapping("/member/device-info")
@Validated
public class DeviceInfoController {

    @Resource
    private DeviceInfoService deviceInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建设备管理")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    @PreAuthorize("@ss.hasPermission('member:device-info:create')")
    public CommonResult<Boolean> createDeviceInfo(@Valid @RequestBody DeviceInfoCreateReqVO createReqVO) {
        deviceInfoService.createDeviceInfo(createReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:device-info:delete')")
    public CommonResult<Boolean> deleteDeviceInfo(@RequestParam("id") Long id) {
        deviceInfoService.deleteDeviceInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:device-info:query')")
    public CommonResult<DeviceInfoRespVO> getDeviceInfo(@RequestParam("id") Long id) {
        DeviceInfoDO deviceInfo = deviceInfoService.getDeviceInfo(id);
        return success(DeviceInfoConvert.INSTANCE.convert(deviceInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得设备管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:device-info:query')")
    public CommonResult<List<DeviceInfoRespVO>> getDeviceInfoList(@RequestParam("ids") Collection<Long> ids) {
        List<DeviceInfoDO> list = deviceInfoService.getDeviceInfoList(ids);
        return success(DeviceInfoConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备管理分页")
    @PreAuthorize("@ss.hasPermission('member:device-info:query')")
    public CommonResult<PageResult<DeviceInfoRespVO>> getDeviceInfoPage(@Valid DeviceInfoPageReqVO pageVO) {
        return success(deviceInfoService.getDeviceInfoPage(pageVO,true));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:device-info:export')")
    @OperateLog(type = EXPORT)
    public void exportDeviceInfoExcel(@Valid DeviceInfoExportReqVO exportReqVO, HttpServletResponse response) throws IOException {
        List<DeviceInfoDO> list = deviceInfoService.getDeviceInfoList(exportReqVO);
        // 导出 Excel
        List<DeviceInfoExcelVO> datas = DeviceInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "设备管理.xls", "数据", DeviceInfoExcelVO.class, datas);
    }

    @PostMapping("/bind")
    @Operation(summary = "设备绑定门店/房间")
    @PreAuthorize("@ss.hasPermission('member:device-info:update')")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> bind(@RequestBody @Valid DeviceInfoBindReqVO reqVO) {
        deviceInfoService.bind(reqVO);
        return success(true);
    }

//    @PostMapping("/configWifi")
//    @Operation(summary = "重置设备wifi")
//    @PreAuthorize("@ss.hasPermission('member:device-info:update')")
//    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
//    public CommonResult<Boolean> configWifi(@RequestBody @Valid DeviceInfoConfigWifiReqVO reqVO) {
//        deviceInfoService.configWifi(reqVO);
//        return success(true);
//    }


    @PostMapping("/setAutoLock")
    @Operation(summary = "设置门锁自动关锁时间")
    @PreAuthorize("@ss.hasPermission('member:device-info:update')")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> setAutoLock(@RequestBody @Valid DeviceInfoSetAutoLockReqVO reqVO) {
        deviceInfoService.setLockAutoLock(reqVO);
        return success(true);
    }


    @PostMapping("/control")
    @Operation(summary = "设备控制")
    @PreAuthorize("@ss.hasPermission('member:device-info:update')")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> control(@RequestBody @Valid DeviceControlReqVO reqVO) {
        deviceInfoService.control(reqVO);
        return success(true);
    }
}