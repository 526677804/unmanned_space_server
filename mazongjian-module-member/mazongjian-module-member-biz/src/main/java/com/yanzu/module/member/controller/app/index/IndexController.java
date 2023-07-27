package com.yanzu.module.member.controller.app.index;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.index.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.index
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 14:13
 */
@Tag(name = "miniapp - 首页")
@RestController
@RequestMapping("/member/index")
@Validated
@Slf4j
public class IndexController {

    @GetMapping("/getCityList")
    @Operation(summary = "获取城市列表", description = "首页使用")
    @PreAuthenticated
    public CommonResult<List<String>> getCityList() {
        return null;
    }

    @GetMapping("/getBannerList")
    @Operation(summary = "获取首页顶部banner图片", description = "首页使用")
    @PreAuthenticated
    public CommonResult<List<String>> getBannerList() {
        return null;
    }

    @PostMapping("/getStoreList")
    @Operation(summary = "获取门店列表", description = "首页使用")
    @PreAuthenticated
    public CommonResult<List<AppStorePageRespVO>> getStoreList(@RequestBody AppStorePageReqVO reqVO) {
        return null;
    }


    @GetMapping("/getStoreInfo/{storeId}")
    @Operation(summary = "获取门店信息详情")
    @PreAuthenticated
    public CommonResult<AppIndexStoreInfoRespVO> getStoreInfo(@PathVariable("storeId") Long storeId) {
        return null;
    }

    @GetMapping("/getStoreList")
    @Operation(summary = "获取门店下拉选择列表")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String,Long>>> getStoreList() {
        return null;
    }

    @GetMapping("/getRoomList/{storeId}")
    @Operation(summary = "获取房间下拉选择列表")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String,Long>>> getRoomList(@PathVariable("storeId") Long storeId) {
        return null;
    }


    @PostMapping("/getRoomInfoList/{storeId}")
    @Operation(summary = "获取房间信息列表")
    @PreAuthenticated
    public CommonResult<List<AppRoomInfoListRespVO>> getRoomInfoList(@PathVariable("storeId") Long storeId) {
        return null;
    }


}
