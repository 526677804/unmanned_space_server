package com.yanzu.module.member.controller.app.index;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.index.vo.*;
import com.yanzu.module.member.service.index.IndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

import static com.yanzu.framework.common.pojo.CommonResult.success;

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

    @Resource
    private IndexService indexService;

    @GetMapping("/getCityList")
    @Operation(summary = "获取城市列表", description = "首页使用")
    public CommonResult<List<String>> getCityList() {
        return success(indexService.getCityList());
    }

    @GetMapping("/getBannerList")
    @Operation(summary = "获取首页顶部banner图片", description = "首页使用")
    public CommonResult<List<AppBannerInfoRespVO>> getBannerList() {
        return success(indexService.getBannerList());
    }

    @PostMapping("/getStoreList")
    @Operation(summary = "首页获取门店列表", description = "首页使用")
    public CommonResult<PageResult<AppStorePageRespVO>> getStorePageList(@RequestBody AppStorePageReqVO reqVO) {
        return success(indexService.getStorePageList(reqVO));
    }

    @GetMapping("/getStoreInfo/{storeId}")
    @Operation(summary = "首页获取门店信息详情")
    @Parameter(name = "storeId")
    public CommonResult<AppIndexStoreInfoRespVO> getStoreInfo(@PathVariable("storeId") Long storeId
            , @RequestParam(value = "lat", required = false) String lat, @RequestParam(value = "lon", required = false) String lon) {
        return success(indexService.getStoreInfo(storeId, lat, lon));
    }


    @PostMapping("/getRoomInfoList")
    @Operation(summary = "首页获取房间信息列表")
    @Parameter(name = "storeId")
    public CommonResult<List<AppRoomInfoListRespVO>> getRoomInfoList(@RequestBody @Valid AppRoomListReqVO reqVO) {
        return success(indexService.getRoomInfoList(reqVO));
    }

    @PostMapping("/getRoomInfoList/{storeId}")
    @Operation(summary = "首页获取房间信息列表（旧）")
    @Parameter(name = "storeId")
    public CommonResult<List<AppRoomInfoListRespVO>> getRoomInfoList(@PathVariable(required = false, name = "storeId") Long storeId
            , @RequestParam(required = false, name = "roomClass") Integer roomClass) {
        AppRoomListReqVO reqVO = new AppRoomListReqVO().setStoreId(storeId).setRoomClass(roomClass);
        return success(indexService.getRoomInfoList(reqVO));
    }


    @PostMapping("/getRoomInfo/{roomId}")
    @Operation(summary = "首页获取房间信息详情")
    @Parameter(name = "roomId")
    public CommonResult<AppRoomInfoListRespVO> getRoomInfo(@PathVariable("roomId") Long roomId) {
        return success(indexService.getRoomInfo(roomId));
    }

    @GetMapping("/getStoreList")
    @Operation(summary = "获取门店下拉选择列表")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, Long>>> getStoreList(@RequestParam(value = "name", required = false) String name
            , @RequestParam(value = "cityName", required = false) String cityName) {
        return success(indexService.getStoreList(name, cityName));
    }

    @GetMapping("/getRoomList/{storeId}")
    @Operation(summary = "获取房间下拉选择列表")
    @PreAuthenticated
    @Parameter(name = "storeId")
    public CommonResult<List<KeyValue<String, Long>>> getRoomList(@PathVariable("storeId") Long storeId) {
        return success(indexService.getRoomList(storeId));
    }


    @GetMapping("/getSysInfo")
    @Operation(summary = "获取系统信息")
    public CommonResult<AppSysInfoRespVO> getSysInfo() {
        return success(indexService.getSysInfo());
    }
}
