package com.yanzu.module.member.controller.admin.facerecord;

import com.dtflys.forest.annotation.Post;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.admin.faceblacklist.vo.FaceBlacklistAddReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppMoveBlacklistReqVO;
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

import com.yanzu.module.member.controller.admin.facerecord.vo.*;
import com.yanzu.module.member.dal.dataobject.facerecord.FaceRecordDO;
import com.yanzu.module.member.convert.facerecord.FaceRecordConvert;
import com.yanzu.module.member.service.facerecord.FaceRecordService;

@Tag(name = "管理后台 - 人脸识别记录")
@RestController
@RequestMapping("/member/face-record")
@Validated
public class FaceRecordController {

    @Resource
    private FaceRecordService faceRecordService;

    @DeleteMapping("/delete")
    @Operation(summary = "删除人脸识别记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:face-record:delete')")
    public CommonResult<Boolean> deleteFaceRecord(@RequestParam("id") Long id) {
        faceRecordService.deleteFaceRecord(id);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得人脸识别记录分页")
    @PreAuthorize("@ss.hasPermission('member:face-record:query')")
    public CommonResult<PageResult<FaceRecordRespVO>> getFaceRecordPage(@Valid FaceRecordPageReqVO pageVO) {
        return success(faceRecordService.getFaceRecordPage(pageVO,true));
    }

    @PostMapping("/moveFaceByRecord")
    @Operation(summary = "根据人脸识别记录修改黑名单")
    @PreAuthenticated
    public CommonResult<Boolean> moveFaceByRecord(@RequestBody @Valid AppMoveBlacklistReqVO reqVO) {
        faceRecordService.moveFaceByRecord(reqVO.getId(), reqVO.getRemark(),true);
        return success(true);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出人脸识别记录 Excel")
    @PreAuthorize("@ss.hasPermission('member:face-record:export')")
    @OperateLog(type = EXPORT)
    public void exportFaceRecordExcel(@Valid FaceRecordExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<FaceRecordDO> list = faceRecordService.getFaceRecordList(exportReqVO);
        // 导出 Excel
        List<FaceRecordExcelVO> datas = FaceRecordConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "人脸识别记录.xls", "数据", FaceRecordExcelVO.class, datas);
    }

}
