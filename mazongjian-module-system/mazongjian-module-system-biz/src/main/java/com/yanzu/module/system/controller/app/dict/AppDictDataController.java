package com.yanzu.module.system.controller.app.dict;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.system.controller.admin.dict.vo.data.DictDataSimpleRespVO;
import com.yanzu.module.system.convert.dict.DictDataConvert;
import com.yanzu.module.system.dal.dataobject.dict.DictDataDO;
import com.yanzu.module.system.service.dict.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.yanzu.framework.common.pojo.CommonResult.success;

@Tag(name = "miniapp  - 获取全部数据字典")
@RestController
@RequestMapping("/member/dict")
@Validated
@Slf4j
public class AppDictDataController {

    @Resource
    private DictDataService dictDataService;

    @GetMapping("/list-all-simple")
    @Operation(summary = "获得全部字典数据列表", description = "一般用于管理后台缓存字典数据在本地")
    // 无需添加权限认证，因为前端全局都需要
    public CommonResult<List<DictDataSimpleRespVO>> getSimpleDictDataList() {
        List<DictDataDO> list = dictDataService.getDictDataList();
        return success(DictDataConvert.INSTANCE.convertList(list));
    }

}
