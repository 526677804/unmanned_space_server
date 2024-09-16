package com.yanzu.module.member.controller.admin.faceblacklist;

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

import com.yanzu.module.member.controller.admin.faceblacklist.vo.*;
import com.yanzu.module.member.dal.dataobject.faceblacklist.FaceBlacklistDO;
import com.yanzu.module.member.convert.faceblacklist.FaceBlacklistConvert;
import com.yanzu.module.member.service.faceblacklist.FaceBlacklistService;

@Tag(name = "管理后台 - 人脸黑名单")
@RestController
@RequestMapping("/member/face-blacklist")
@Validated
public class FaceBlacklistController {

    @Resource
    private FaceBlacklistService faceBlacklistService;


    @DeleteMapping("/delete")
    @Operation(summary = "删除人脸黑名单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:face-blacklist:delete')")
    public CommonResult<Boolean> deleteFaceBlacklist(@RequestParam("id") Long id) {
        faceBlacklistService.deleteFaceBlacklist(id);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得人脸黑名单分页")
    @PreAuthorize("@ss.hasPermission('member:face-blacklist:query')")
    public CommonResult<PageResult<FaceBlacklistRespVO>> getFaceBlacklistPage(@Valid FaceBlacklistPageReqVO pageVO) {
        return success(faceBlacklistService.getFaceBlacklistPage(pageVO,true));
    }


}
