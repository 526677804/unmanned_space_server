package com.yanzu.module.member.controller.admin.roominfo;

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

import com.yanzu.module.member.controller.admin.roominfo.vo.*;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.convert.roominfo.RoomInfoConvert;
import com.yanzu.module.member.service.roominfo.RoomInfoService;

@Tag(name = "管理后台 - 房间管理")
@RestController
@RequestMapping("/member/room-info")
@Validated
public class RoomInfoController {

    @Resource
    private RoomInfoService roomInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建房间管理")
    @PreAuthorize("@ss.hasPermission('member:room-info:create')")
    public CommonResult<Long> createRoomInfo(@Valid @RequestBody RoomInfoCreateReqVO createReqVO) {
        return success(roomInfoService.createRoomInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新房间管理")
    @PreAuthorize("@ss.hasPermission('member:room-info:update')")
    public CommonResult<Boolean> updateRoomInfo(@Valid @RequestBody RoomInfoUpdateReqVO updateReqVO) {
        roomInfoService.updateRoomInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除房间管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:room-info:delete')")
    public CommonResult<Boolean> deleteRoomInfo(@RequestParam("id") Long id) {
        roomInfoService.deleteRoomInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得房间管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:room-info:query')")
    public CommonResult<RoomInfoRespVO> getRoomInfo(@RequestParam("id") Long id) {
        RoomInfoDO roomInfo = roomInfoService.getRoomInfo(id);
        return success(RoomInfoConvert.INSTANCE.convert(roomInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得房间管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:room-info:query')")
    public CommonResult<List<RoomInfoRespVO>> getRoomInfoList(@RequestParam("ids") Collection<Long> ids) {
        List<RoomInfoDO> list = roomInfoService.getRoomInfoList(ids);
        return success(RoomInfoConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得房间管理分页")
    @PreAuthorize("@ss.hasPermission('member:room-info:query')")
    public CommonResult<PageResult<RoomInfoRespVO>> getRoomInfoPage(@Valid RoomInfoPageReqVO pageVO) {
        PageResult<RoomInfoDO> pageResult = roomInfoService.getRoomInfoPage(pageVO);
        return success(RoomInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出房间管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:room-info:export')")
    @OperateLog(type = EXPORT)
    public void exportRoomInfoExcel(@Valid RoomInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<RoomInfoDO> list = roomInfoService.getRoomInfoList(exportReqVO);
        // 导出 Excel
        List<RoomInfoExcelVO> datas = RoomInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "房间管理.xls", "数据", RoomInfoExcelVO.class, datas);
    }

}
