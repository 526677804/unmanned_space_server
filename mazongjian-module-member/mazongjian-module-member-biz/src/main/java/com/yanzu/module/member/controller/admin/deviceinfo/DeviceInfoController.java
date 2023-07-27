package com.yanzu.module.member.controller.admin.deviceinfo;

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

import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.module.member.convert.deviceinfo.DeviceInfoConvert;
import com.yanzu.module.member.service.deviceinfo.DeviceInfoService;

@Tag(name = "管理后台 - 设备管理")
@RestController
@RequestMapping("/member/device-info")
@Validated
public class DeviceInfoController {

    @Resource
    private DeviceInfoService deviceInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建设备管理")
    @PreAuthorize("@ss.hasPermission('member:device-info:create')")
    public CommonResult<Long> createDeviceInfo(@Valid @RequestBody DeviceInfoCreateReqVO createReqVO) {
        return success(deviceInfoService.createDeviceInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备管理")
    @PreAuthorize("@ss.hasPermission('member:device-info:update')")
    public CommonResult<Boolean> updateDeviceInfo(@Valid @RequestBody DeviceInfoUpdateReqVO updateReqVO) {
        deviceInfoService.updateDeviceInfo(updateReqVO);
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
        PageResult<DeviceInfoDO> pageResult = deviceInfoService.getDeviceInfoPage(pageVO);
        return success(DeviceInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:device-info:export')")
    @OperateLog(type = EXPORT)
    public void exportDeviceInfoExcel(@Valid DeviceInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<DeviceInfoDO> list = deviceInfoService.getDeviceInfoList(exportReqVO);
        // 导出 Excel
        List<DeviceInfoExcelVO> datas = DeviceInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "设备管理.xls", "数据", DeviceInfoExcelVO.class, datas);
    }

}
