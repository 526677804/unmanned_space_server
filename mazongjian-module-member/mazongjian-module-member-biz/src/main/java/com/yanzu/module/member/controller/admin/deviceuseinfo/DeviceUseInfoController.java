package com.yanzu.module.member.controller.admin.deviceuseinfo;

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

import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;
import com.yanzu.module.member.convert.deviceuseinfo.DeviceUseInfoConvert;
import com.yanzu.module.member.service.deviceuseinfo.DeviceUseInfoService;

@Tag(name = "管理后台 - 设备使用记录")
@RestController
@RequestMapping("/member/device-use-info")
@Validated
public class DeviceUseInfoController {

    @Resource
    private DeviceUseInfoService deviceUseInfoService;

    @GetMapping("/get")
    @Operation(summary = "获得设备使用记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:device-use-info:query')")
    public CommonResult<DeviceUseInfoRespVO> getDeviceUseInfo(@RequestParam("id") Long id) {
        DeviceUseInfoDO deviceUseInfo = deviceUseInfoService.getDeviceUseInfo(id);
        return success(DeviceUseInfoConvert.INSTANCE.convert(deviceUseInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得设备使用记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:device-use-info:query')")
    public CommonResult<List<DeviceUseInfoRespVO>> getDeviceUseInfoList(@RequestParam("ids") Collection<Long> ids) {
        List<DeviceUseInfoDO> list = deviceUseInfoService.getDeviceUseInfoList(ids);
        return success(DeviceUseInfoConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备使用记录分页")
    @PreAuthorize("@ss.hasPermission('member:device-use-info:query')")
    public CommonResult<PageResult<DeviceUseInfoRespVO>> getDeviceUseInfoPage(@Valid DeviceUseInfoPageReqVO pageVO) {
        PageResult<DeviceUseInfoRespVO> pageResult = deviceUseInfoService.getDeviceUseInfoPage(pageVO);
        return success(pageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备使用记录 Excel")
    @PreAuthorize("@ss.hasPermission('member:device-use-info:export')")
    @OperateLog(type = EXPORT)
    public void exportDeviceUseInfoExcel(@Valid DeviceUseInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<DeviceUseInfoDO> list = deviceUseInfoService.getDeviceUseInfoList(exportReqVO);
        // 导出 Excel
        List<DeviceUseInfoExcelVO> datas = DeviceUseInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "设备使用记录.xls", "数据", DeviceUseInfoExcelVO.class, datas);
    }

}
