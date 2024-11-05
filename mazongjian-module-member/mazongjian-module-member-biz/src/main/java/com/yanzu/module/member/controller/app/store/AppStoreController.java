package com.yanzu.module.member.controller.app.store;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.admin.faceblacklist.vo.FaceBlacklistPageReqVO;
import com.yanzu.module.member.controller.admin.faceblacklist.vo.FaceBlacklistRespVO;
import com.yanzu.module.member.controller.admin.facerecord.vo.FaceRecordPageReqVO;
import com.yanzu.module.member.controller.admin.facerecord.vo.FaceRecordRespVO;
import com.yanzu.module.member.controller.app.order.vo.ControlKTReqVO;
import com.yanzu.module.member.controller.app.store.vo.*;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.faceblacklist.FaceBlacklistService;
import com.yanzu.module.member.service.facerecord.FaceRecordService;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.common.pojo.CommonResult.success;
import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yanzu.module.infra.enums.ErrorCodeConstants.FILE_IS_EMPTY;

@Tag(name = "miniapp - 门店管理 （管理员）")
@RestController
@RequestMapping("/member/store")
@Validated
@Slf4j
public class AppStoreController {

    @Resource
    private StoreInfoService storeInfoService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private FaceRecordService faceRecordService;

    @Resource
    private FaceBlacklistService faceBlacklistService;

    @PostMapping("/getPageList")
    @Operation(summary = "获取门店列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppStoreAdminRespVO>> getPageList(@RequestBody @Valid AppStoreAdminReqVO reqVO) {
        return success(storeInfoService.getPageList(reqVO));
    }


    @GetMapping("/getDetail/{storeId}")
    @Operation(summary = "获取门店详情")
    @PreAuthenticated
    @Parameter(name = "storeId")
    public CommonResult<AppStoreInfoRespVO> getDetail(@PathVariable("storeId") Long storeId) {
        return success(storeInfoService.getDetail(storeId));
    }

    @PostMapping("/save")
    @Operation(summary = "保存门店详情")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> save(@RequestBody @Valid AppStoreInfoReqVO reqVO) {
        storeInfoService.save(reqVO);
        return success(true);
    }

    @PostMapping("/openStoreDoor/{storeId}")
    @Operation(summary = "开门店的大门", description = "门店管理使用")
    @PreAuthenticated
    @Parameter(name = "storeId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> openStoreDoor(@PathVariable("storeId") Long storeId) {
        //1用户开门 2管理员开门 3保洁开门
        deviceService.openStoreDoor(getLoginUserId(), storeId, 2);
        return success(true);
    }

    @GetMapping("/getRoomInfoList/{storeId}")
    @Operation(summary = "获取门店的房间列表")
    @PreAuthenticated
    @Parameter(name = "storeId")
    public CommonResult<List<AppRoomListRespVO>> getRoomInfoList(@PathVariable("storeId") Long storeId) {
        return success(storeInfoService.getRoomInfoList(storeId));
    }

    @PostMapping("/getRoomInfoList")
    @Operation(summary = "获取门店的房间列表")
    @PreAuthenticated
    @Parameter(name = "storeId")
    public CommonResult<List<AppRoomInfoListRespVO>> getRoomInfoList2(@RequestParam(value = "storeId", required = false) Long storeId) {
        return success(storeInfoService.getRoomInfoList2(storeId));
    }

    @GetMapping("/getRoomDetail/{roomId}")
    @Operation(summary = "获取房间详情")
    @PreAuthenticated
    @Parameter(name = "roomId")
    public CommonResult<AppRoomDetailRespVO> getRoomDetail(@PathVariable("roomId") Long roomId) {
        return success(storeInfoService.getRoomDetail(roomId));
    }

    @PostMapping("/saveRoomDetail")
    @Operation(summary = "保存房间详情")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> saveRoomDetail(@RequestBody @Valid AppRoomDetailReqVO reqVO) {
        storeInfoService.saveRoomDetail(reqVO);
        return success(true);
    }

    @PostMapping("/getGroupPayAuthUrl")
    @Operation(summary = "获取团购平台授权链接")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<String> getGroupPayAuthUrl(@RequestBody @Valid GroupPayAuthUrlReqVO reqVO) {
        return success(storeInfoService.getGroupPayAuthUrl(reqVO));
    }

    @PostMapping("/testYunlaba/{roomId}")
    @Operation(summary = "测试云喇叭", description = "房间管理使用")
    @PreAuthenticated
    @Parameter(name = "roomId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> testYunlaba(@PathVariable("roomId") Long roomId) {
        deviceService.testYunlaba(roomId);
        return success(true);
    }


    @PostMapping("/addDevice")
    @Operation(summary = "添加设备", description = "房间管理使用")
    @PreAuthenticated
    @Parameter(name = "roomId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> addDevice(@RequestBody @Validated AppAddDeviceReqVO reqVO) {
        storeInfoService.addDevice(reqVO);
        return success(true);
    }

    @PostMapping("/delDevice/{deviceId}")
    @Operation(summary = "删除设备", description = "房间管理使用")
    @PreAuthenticated
    @Parameter(name = "roomId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> delDevice(@PathVariable("deviceId") Long deviceId) {
        storeInfoService.delDevice(deviceId);
        return success(true);
    }


    @PostMapping("/deleteRoomInfo/{roomId}")
    @Operation(summary = "删除房间", description = "房间管理使用")
    @PreAuthenticated
    @Parameter(name = "roomId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> deleteRoomInfo(@PathVariable("roomId") Long roomId) {
        storeInfoService.deleteRoomInfo(roomId);
        return success(true);
    }


    @PostMapping("/openRoomDoor/{roomId}")
    @Operation(summary = "开房间电源和灯光", description = "房间管理使用")
    @PreAuthenticated
    @Parameter(name = "roomId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> openRoomDoor(@PathVariable("roomId") Long roomId) {
        //1用户开门 2管理员开门 3保洁开门
        deviceService.openRoomDoor(getLoginUserId(), null, roomId, 2);
        return success(true);
    }


    @PostMapping("/openRoomLock/{roomId}")
    @Operation(summary = "开房间的门锁", description = "房间管理使用")
    @PreAuthenticated
    @Parameter(name = "roomId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> openRoomLock(@PathVariable("roomId") Long roomId) {
        //1用户开门 2管理员开门 3保洁开门
        deviceService.openRoomBlueLock(getLoginUserId(), null, roomId, 2);
        return success(true);
    }

    @PostMapping("/closeRoomDoor/{roomId}")
    @Operation(summary = "关房间的门和电源", description = "房间管理使用")
    @PreAuthenticated
    @Parameter(name = "roomId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> closeRoomDoor(@PathVariable("roomId") Long roomId) {
        //1用户关门 2管理员关门 3保洁关门
        deviceService.closeRoomDoor(getLoginUserId(), null, roomId, 2);
        return success(true);
    }

    @PostMapping("/disableRoom/{roomId}")
    @Operation(summary = "启用禁用房间", description = "房间管理使用")
    @PreAuthenticated
    @Parameter(name = "roomId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> disableRoom(@PathVariable("roomId") Long roomId) {
        storeInfoService.disableRoom(roomId);
        return success(true);
    }


    @PostMapping("/getDiscountRulesPage")
    @Operation(summary = "获取门店充值优惠信息分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppDiscountRulesPageRespVO>> getDiscountRulesPage(@RequestBody @Valid AppDiscountRulesPageReqVO reqVO) {
        return success(storeInfoService.getDiscountRulesPage(reqVO));
    }

    @PostMapping("/changeDiscountRulesStatus/{discountId}")
    @Operation(summary = "修改门店充值优惠信息状态（启用/禁用）")
    @PreAuthenticated
    @Parameter(name = "discountId")
    public CommonResult<Boolean> changeDiscountRulesStatus(@PathVariable("discountId") Long discountId) {
        storeInfoService.changeDiscountRulesStatus(discountId);
        return success(true);
    }

    @GetMapping("/getDiscountRuleDetail/{discountId}")
    @Operation(summary = "获取门店充值优惠信息详情")
    @PreAuthenticated
    @Parameter(name = "discountId")
    public CommonResult<AppDiscountRulesDetailRespVO> getDiscountRuleDetail(@PathVariable("discountId") Long discountId) {
        return success(storeInfoService.getDiscountRuleDetail(discountId));
    }

    @PostMapping("/saveDiscountRuleDetail")
    @Operation(summary = "保存门店充值优惠信息")
    @PreAuthenticated
    public CommonResult<Boolean> saveDiscountRuleDetail(@RequestBody @Valid AppDiscountRulesDetailReqVO reqVO) {
        storeInfoService.saveDiscountRuleDetail(reqVO);
        return success(true);
    }


    @PostMapping("/uploadImg")
    @Operation(summary = "上传图片，并返回图片访问地址")
    @PreAuthenticated
    public CommonResult<String> uploadImg(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_IS_EMPTY);
        }
        String avatar = storeInfoService.uploadImg(file.getInputStream());
        return success(avatar);
    }

    @GetMapping("/getStoreList")
    @Operation(summary = "管理员获取门店下拉选择列表")
    @PreAuthenticated
    public CommonResult<List<KeyValue<String, Long>>> getStoreList(@RequestParam(value = "name", required = false) String name
            , @RequestParam(value = "cityName", required = false) String cityName) {
        return success(storeInfoService.getStoreList(name, cityName));
    }

    @GetMapping("/getRoomList/{storeId}")
    @Operation(summary = "管理员获取房间下拉选择列表")
    @PreAuthenticated
    @Parameter(name = "storeId")
    public CommonResult<List<KeyValue<String, Long>>> getRoomList(@PathVariable("storeId") Long storeId) {
        return success(storeInfoService.getRoomList(storeId));
    }

    @GetMapping("/clearAndFinish/{roomId}")
    @Operation(summary = "管理员一键清洁并结单")
    @PreAuthenticated
    @Parameter(name = "roomId")
    public CommonResult<Boolean> clearAndFinish(@PathVariable("roomId") Long roomId) {
        storeInfoService.clearAndFinish(roomId);
        return success(true);
    }

    @GetMapping("/finishRoomOrder/{roomId}")
    @Operation(summary = "管理员对指定房间结单")
    @PreAuthenticated
    @Parameter(name = "roomId")
    public CommonResult<Boolean> finishRoomOrder(@PathVariable("roomId") Long roomId) {
        storeInfoService.finishRoomOrder(roomId);
        return success(true);
    }

    @GetMapping("/workPrice/{storeId}")
    @Operation(summary = "管理员启用/关闭工作日价格")
    @PreAuthenticated
    @Parameter(name = "storeId")
    public CommonResult<Boolean> workPrice(@PathVariable("storeId") Long storeId) {
        storeInfoService.syncPrice(storeId);
        return success(true);
    }

//    @PostMapping("/updateLockData")
//    @Operation(summary = "更新锁数据")
//    @PreAuthenticated
//    public CommonResult<Boolean> updateLockData(@PathVariable("storeId") Long storeId) {
//        return success(true);
//    }
//


    @GetMapping("/getStoreSoundInfo/{storeId}")
    @Operation(summary = "获取门店语音播报设置")
    @PreAuthenticated
    @Parameter(name = "storeId")
    public CommonResult<AppStoreSoundInfoRespVO> getStoreSoundInfo(@PathVariable("storeId") Long storeId) {
        return success(storeInfoService.getStoreSoundInfo(storeId));
    }

    @PostMapping("/saveStoreSoundInfo")
    @Operation(summary = "保存门店语音播报设置")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> saveStoreSoundInfo(@RequestBody @Valid AppStoreSoundInfoReqVO reqVO) {
        storeInfoService.saveStoreSoundInfo(reqVO);
        return success(true);
    }

    @PostMapping("/getFaceRecordPage")
    @Operation(summary = "获得人脸识别记录分页")
    @PreAuthenticated
    public CommonResult<PageResult<FaceRecordRespVO>> getFaceRecordPage(@RequestBody @Valid FaceRecordPageReqVO pageVO) {
        return success(faceRecordService.getFaceRecordPage(pageVO, false));
    }

    @PostMapping("/getFaceBlacklistPage")
    @Operation(summary = "获得人脸黑名单分页")
    @PreAuthenticated
    public CommonResult<PageResult<FaceBlacklistRespVO>> getFaceBlacklistPage(@RequestBody @Valid FaceBlacklistPageReqVO pageVO) {
        return success(faceBlacklistService.getFaceBlacklistPage(pageVO, false));
    }

    @PostMapping("/moveFaceByRecord")
    @Operation(summary = "根据人脸识别记录修改黑名单")
    @PreAuthenticated
    @Idempotent(timeout = 3,message = "你的点击太快了~")
    public CommonResult<Boolean> moveFaceByRecord(@RequestBody @Valid AppMoveBlacklistReqVO reqVO) {
        faceRecordService.moveFaceByRecord(reqVO.getId(), reqVO.getRemark(),false);
        return success(true);
    }

    @PostMapping("/moveFaceById/{id}")
    @Operation(summary = "移出人脸识黑名单")
    @PreAuthenticated
    @Idempotent(timeout = 3,message = "你的点击太快了~")
    public CommonResult<Boolean> moveFaceById(@PathVariable("id") Long id) {
        faceBlacklistService.moveFaceById(id);
        return success(true);
    }

    @PostMapping("/controlKT")
    @Operation(summary = "管理员控制空调", description = "房间控制使用")
    @PreAuthenticated
    @Idempotent(timeout = 100, timeUnit = TimeUnit.MILLISECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> controlKT(@RequestBody @Valid AppStoreControlKTReqVO reqVO) {
        storeInfoService.controlKT(reqVO);
        return success(true);
    }

    @PostMapping("/addLock")
    @Operation(summary = "添加智能锁", description = "设备管理使用")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> addLock(@RequestBody @Valid AppAddLockReqVO reqVO) {
        storeInfoService.addLock(reqVO);
        return success(true);
    }

}
