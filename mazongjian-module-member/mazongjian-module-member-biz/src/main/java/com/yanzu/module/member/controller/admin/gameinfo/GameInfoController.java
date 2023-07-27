package com.yanzu.module.member.controller.admin.gameinfo;

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

import com.yanzu.module.member.controller.admin.gameinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.gameinfo.GameInfoDO;
import com.yanzu.module.member.convert.gameinfo.GameInfoConvert;
import com.yanzu.module.member.service.gameinfo.GameInfoService;

@Tag(name = "管理后台 - 在线组局管理")
@RestController
@RequestMapping("/member/game-info")
@Validated
public class GameInfoController {

    @Resource
    private GameInfoService gameInfoService;

    @DeleteMapping("/delete")
    @Operation(summary = "删除在线组局管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:game-info:delete')")
    public CommonResult<Boolean> deleteGameInfo(@RequestParam("id") Long id) {
        gameInfoService.deleteGameInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得在线组局管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:game-info:query')")
    public CommonResult<GameInfoRespVO> getGameInfo(@RequestParam("id") Long id) {
        GameInfoDO gameInfo = gameInfoService.getGameInfo(id);
        return success(GameInfoConvert.INSTANCE.convert(gameInfo));
    }


    @GetMapping("/page")
    @Operation(summary = "获得在线组局管理分页")
    @PreAuthorize("@ss.hasPermission('member:game-info:query')")
    public CommonResult<PageResult<GameInfoRespVO>> getGameInfoPage(@Valid GameInfoPageReqVO pageVO) {
        PageResult<GameInfoDO> pageResult = gameInfoService.getGameInfoPage(pageVO);
        return success(GameInfoConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出在线组局管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:game-info:export')")
    @OperateLog(type = EXPORT)
    public void exportGameInfoExcel(@Valid GameInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<GameInfoDO> list = gameInfoService.getGameInfoList(exportReqVO);
        // 导出 Excel
        List<GameInfoExcelVO> datas = GameInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "在线组局管理.xls", "数据", GameInfoExcelVO.class, datas);
    }

}
