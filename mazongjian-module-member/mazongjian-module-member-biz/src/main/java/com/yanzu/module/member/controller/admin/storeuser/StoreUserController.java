package com.yanzu.module.member.controller.admin.storeuser;

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

import com.yanzu.module.member.controller.admin.storeuser.vo.*;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.convert.storeuser.StoreUserConvert;
import com.yanzu.module.member.service.storeuser.StoreUserService;

@Tag(name = "管理后台 - 门店用户管理")
@RestController
@RequestMapping("/member/store-user")
@Validated
public class StoreUserController {

    @Resource
    private StoreUserService storeUserService;

    @PostMapping("/create")
    @Operation(summary = "创建门店用户管理")
    @PreAuthorize("@ss.hasPermission('member:store-user:create')")
    public CommonResult<Long> createStoreUser(@Valid @RequestBody StoreUserCreateReqVO createReqVO) {
        return success(storeUserService.createStoreUser(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新门店用户管理")
    @PreAuthorize("@ss.hasPermission('member:store-user:update')")
    public CommonResult<Boolean> updateStoreUser(@Valid @RequestBody StoreUserUpdateReqVO updateReqVO) {
        storeUserService.updateStoreUser(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除门店用户管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:store-user:delete')")
    public CommonResult<Boolean> deleteStoreUser(@RequestParam("id") Long id) {
        storeUserService.deleteStoreUser(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得门店用户管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:store-user:query')")
    public CommonResult<StoreUserRespVO> getStoreUser(@RequestParam("id") Long id) {
        StoreUserDO storeUser = storeUserService.getStoreUser(id);
        return success(StoreUserConvert.INSTANCE.convert(storeUser));
    }

    @GetMapping("/list")
    @Operation(summary = "获得门店用户管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('member:store-user:query')")
    public CommonResult<List<StoreUserRespVO>> getStoreUserList(@RequestParam("ids") Collection<Long> ids) {
        List<StoreUserDO> list = storeUserService.getStoreUserList(ids);
        return success(StoreUserConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得门店用户管理分页")
    @PreAuthorize("@ss.hasPermission('member:store-user:query')")
    public CommonResult<PageResult<StoreUserRespVO>> getStoreUserPage(@Valid StoreUserPageReqVO pageVO) {
        PageResult<StoreUserDO> pageResult = storeUserService.getStoreUserPage(pageVO);
        return success(StoreUserConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出门店用户管理 Excel")
    @PreAuthorize("@ss.hasPermission('member:store-user:export')")
    @OperateLog(type = EXPORT)
    public void exportStoreUserExcel(@Valid StoreUserExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<StoreUserDO> list = storeUserService.getStoreUserList(exportReqVO);
        // 导出 Excel
        List<StoreUserExcelVO> datas = StoreUserConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "门店用户管理.xls", "数据", StoreUserExcelVO.class, datas);
    }

}
