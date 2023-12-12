package com.yanzu.module.member.controller.admin.groupPay;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.excel.core.util.ExcelUtils;
import com.yanzu.framework.operatelog.core.annotations.OperateLog;
import com.yanzu.module.member.controller.admin.groupPay.vo.*;
import com.yanzu.module.member.convert.member.GroupPayInfoConvert;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import com.yanzu.module.member.service.groupPay.GroupPayInfoService;
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

import static com.yanzu.framework.common.pojo.CommonResult.success;
import static com.yanzu.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 团购支付信息")
@RestController
@RequestMapping("/member/group-pay-info")
@Validated
public class GroupPayInfoController {

    @Resource
    private GroupPayInfoService groupPayInfoService;

    @GetMapping("/get")
    @Operation(summary = "获得团购支付信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:group-pay-info:query')")
    public CommonResult<GroupPayInfoRespVO> getGroupPayInfo(@RequestParam("id") Long id) {
        GroupPayInfoDO groupPayInfo = groupPayInfoService.getGroupPayInfo(id);
        return success(GroupPayInfoConvert.INSTANCE.convert(groupPayInfo));
    }

    @GetMapping("/list")
    @Operation(summary = "获得团购支付信息列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:group-pay-info:query')")
    public CommonResult<List<GroupPayInfoRespVO>> getGroupPayInfoList(@RequestParam("ids") Collection<Long> ids) {
        List<GroupPayInfoDO> list = groupPayInfoService.getGroupPayInfoList(ids);
        return success(GroupPayInfoConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得团购支付信息分页")
    @PreAuthorize("@ss.hasPermission('member:group-pay-info:query')")
    public CommonResult<PageResult<GroupPayInfoRespVO>> getGroupPayInfoPage(@Valid GroupPayInfoPageReqVO pageVO) {
        return success(groupPayInfoService.getGroupPayInfoPage(pageVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出团购支付信息 Excel")
    @PreAuthorize("@ss.hasPermission('member:group-pay-info:export')")
    @OperateLog(type = EXPORT)
    public void exportGroupPayInfoExcel(@Valid GroupPayInfoExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<GroupPayInfoDO> list = groupPayInfoService.getGroupPayInfoList(exportReqVO);
        // 导出 Excel
        List<GroupPayInfoExcelVO> datas = GroupPayInfoConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "团购支付信息.xls", "数据", GroupPayInfoExcelVO.class, datas);
    }

}
