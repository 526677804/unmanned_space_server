package com.yanzu.module.member.controller.app.store;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.order.vo.OrderListRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderPageReqVO;
import com.yanzu.module.member.controller.app.store.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "miniapp - 门店管理 （管理员）")
@RestController
@RequestMapping("/member/store")
@Validated
@Slf4j
public class AppStoreController {

    @PostMapping("/getPageList")
    @Operation(summary = "获取门店列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppStoreAdminRespVO>> getOrderPage(@RequestBody @Valid AppStoreAdminReqVO reqVO) {
        return null;
    }


    @GetMapping("/getDetail/{storeId}")
    @Operation(summary = "获取门店详情")
    @PreAuthenticated
    public CommonResult<AppStoreInfoRespVO> getDetail(@PathVariable("storeId") Long storeId) {
        return null;
    }


    @PostMapping("/save")
    @Operation(summary = "保存门店详情")
    @PreAuthenticated
    public CommonResult<Boolean> save(@RequestBody @Valid AppStoreInfoReqVO reqVO) {
        return null;
    }


    @GetMapping("/getRoomList/{storeId}")
    @Operation(summary = "获取门店的房间列表")
    @PreAuthenticated
    public CommonResult<List<AppRoomListRespVO>> getRoomList(@PathVariable("storeId") Long storeId) {
        return null;
    }

    @GetMapping("/getRoomDetail/{roomId}")
    @Operation(summary = "获取房间详情")
    @PreAuthenticated
    public CommonResult<AppRoomDetailRespVO> getRoomDetail(@PathVariable("roomId") Long roomId) {
        return null;
    }

    @PostMapping("/saveRoomDetail")
    @Operation(summary = "保存房间详情")
    @PreAuthenticated
    public CommonResult<Boolean> getRoomDetail(@RequestBody @Valid AppRoomDetailReqVO reqVO) {
        return null;
    }

    @PutMapping("/openRoomDoor/{roomId}")
    @Operation(summary = "开房间的大门", description = "房间管理使用")
    @PreAuthenticated
    public CommonResult<Boolean> openRoomDoor(@PathVariable("roomId") Long roomId) {
        return null;
    }

    @PutMapping("/closeRoomDoor/{roomId}")
    @Operation(summary = "关房间的大门", description = "房间管理使用")
    @PreAuthenticated
    public CommonResult<Boolean> closeRoomDoor(@PathVariable("roomId") Long roomId) {
        return null;
    }


    @PostMapping("/getDiscountRulesPage")
    @Operation(summary = "获取门店充值优惠信息分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppDiscountRulesPageRespVO>> getDiscountRulesPage(@RequestBody @Valid AppDiscountRulesPageReqVO reqVO) {
        return null;
    }

    @PutMapping("/changeDiscountRulesStatus/{id}")
    @Operation(summary = "修改门店充值优惠信息状态（启用/禁用）")
    @PreAuthenticated
    public CommonResult<Boolean> changeDiscountRulesStatus(@PathVariable("id") Long id) {
        return null;
    }

    @GetMapping("/getDiscountRulesStatus/{id}")
    @Operation(summary = "获取门店充值优惠信息详情")
    @PreAuthenticated
    public CommonResult<AppDiscountRulesDetailRespVO> getDiscountRulesStatus(@PathVariable("id") Long id) {
        return null;
    }

    @PostMapping("/saveDiscountRulesStatus")
    @Operation(summary = "保存门店充值优惠信息")
    @PreAuthenticated
    public CommonResult<Boolean> saveDiscountRulesStatus(@RequestBody @Valid AppDiscountRulesDetailReqVO reqVO) {
        return null;
    }


}
