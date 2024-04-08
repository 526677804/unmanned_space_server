package com.yanzu.module.member.controller.app.user;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.infra.api.file.FileApi;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.user.vo.*;
import com.yanzu.module.member.dal.mysql.deviceinfo.DeviceInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.service.user.AppUserService;
import com.yanzu.module.member.service.wx.MyWxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yanzu.framework.common.pojo.CommonResult.success;
import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "miniapp - 个人中心")
@RestController
@RequestMapping("/member/user")
@Validated
@Slf4j
public class AppUserController {

    @Resource
    private AppUserService userService;

    @PostMapping("/updateNickname")
    @Operation(summary = "修改用户昵称")
    @PreAuthenticated
    public CommonResult<Boolean> updateUserNickname(@RequestParam("nickname") String nickname) {
        userService.updateUserNickname(getLoginUserId(), nickname);
        return success(true);
    }

    /*  @PostMapping("/updateAvatar")
      @Operation(summary = "修改用户头像")
      @PreAuthenticated
      public CommonResult<String> updateUserAvatar(@RequestParam("avatarFile") MultipartFile file) throws Exception {
          if (file.isEmpty()) {
              throw exception(FILE_IS_EMPTY);
          }
          String avatar = userService.updateUserAvatar(getLoginUserId(), file.getInputStream());
          return success(avatar);
      }*/
    @PostMapping("/updateAvatar")
    @Operation(summary = "修改用户头像")
    @PreAuthenticated
    public CommonResult<Boolean> updateUserAvatar(@RequestParam("avatarUrl") String avatarUrl) {
        userService.updateUserAvatarUrl(getLoginUserId(), avatarUrl);
        return success(true);
    }

    @PostMapping("/update-mobile")
    @Operation(summary = "修改用户手机")
    @PreAuthenticated
    public CommonResult<Boolean> updateMobile(@RequestBody @Valid AppUserUpdateMobileReqVO reqVO) {
        userService.updateUserMobile(getLoginUserId(), reqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得基本信息")
    @PreAuthenticated
    public CommonResult<AppUserInfoRespVO> getUserInfo() {
        return success(userService.getUserInfo(getLoginUserId()));
    }

    @GetMapping("/getGiftBalance/{storeId}")
    @Operation(summary = "获取用户在指定门店的赠送余额")
    @PreAuthenticated
    @Parameter(name = "storeId")
    public CommonResult<BigDecimal> getGiftBalance(@PathVariable("storeId") Long storeId) {
        return success(userService.getGiftBalance(storeId));
    }

    @GetMapping("/getStoreBalance/{storeId}")
    @Operation(summary = "获取用户在指定门店的余额")
    @PreAuthenticated
    @Parameter(name = "storeId")
    public CommonResult<AppStoreBalanceRespVO> getStoreBalance(@PathVariable("storeId") Long storeId) {
        return success(userService.getStoreBalance(storeId));
    }

    @PostMapping("/getBalancePage")
    @Operation(summary = "获取用户账单明细分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppUserMoneyBillRespVO>> getBalancePage(@RequestBody @Valid AppUserMoneyBillPageReqVO reqVO) {
        return success(userService.getBalancePage(reqVO));
    }

    @PostMapping("/getMoneyBillPage")
    @Operation(summary = "获取用户账单明细分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppUserMoneyBillRespVO>> getMoneyBillPage(@RequestBody @Valid AppUserMoneyBillPageReqVO reqVO) {
        return success(userService.getBalancePage(reqVO));
    }

    @GetMapping("/getGiftBalanceList")
    @Operation(summary = "获取赠送余额列表")
    @PreAuthenticated
    public CommonResult<List<AppGiftBalanceListRespVO>> getGiftBalanceList() {
        return success(userService.getGiftBalanceList());
    }

    @GetMapping("/getUserBalanceList")
    @Operation(summary = "获取用户余额列表")
    @PreAuthenticated
    public CommonResult<List<AppGiftBalanceListRespVO>> getUserBalanceList() {
        return success(userService.getGiftBalanceList());
    }

    @PostMapping("/rechargeBalance")
    @Operation(summary = "用户余额充值")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> eechargeBalance(@RequestBody @Valid AppRechargeBalanceReqVO reqVO) {
        userService.eechargeBalance(reqVO);
        return success(true);
    }

    @PostMapping("/preRechargeBalance")
    @Operation(summary = "预下单(微信支付)用户余额充值")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<WxPayOrderRespVO> preRechargeBalance(@RequestBody @Valid AppPreRechargeBalanceReqVO reqVO) {
        return success(userService.preRechargeBalance(reqVO));
    }

    @GetMapping("/getFranchiseInfo")
    @Operation(summary = "获取加盟信息")
    @PreAuthenticated
    public CommonResult<AppFranchiseInfoRespVO> getFranchiseInfo(HttpServletRequest request) {
        return success(userService.getFranchiseInfo(request));
    }


    @PostMapping("/saveFranchiseInfo")
    @Operation(summary = "提交加盟信息")
    @PreAuthenticated
    public CommonResult<Boolean> saveFranchiseInfo(@RequestBody @Valid AppFranchiseInfoReqVO reqVO) {
        userService.saveFranchiseInfo(reqVO);
        return success(true);
    }


    @PostMapping("/getCouponPage")
    @Operation(summary = "获取用户优惠券分页列表")
    @PreAuthenticated
    public CommonResult<PageResult<AppCouponPageRespVO>> getCouponPage(@RequestBody @Valid AppCouponPageReqVO reqVO) {
        return success(userService.getCouponPage(reqVO));
    }


    /**
     * 下面是方便测试写的临时代码  没有任何作用
     */

    @Resource
    private FileApi fileApi;

    @Resource
    private MyWxService myWxService;


//    @Autowired
//    MemberUserApi memberUserApi;

//    @Resource
//    private IotService iotService;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;
    @GetMapping("/test")
    @Operation(summary = "test")
    public CommonResult<String> test() throws Exception {
//        WxMaService wxMaService = myWxService.initWxMa();
//        List<RoomInfoDO> roomInfoDOS = roomInfoMapper.selectList();
//        roomInfoDOS.forEach(x->{
//            // 获取小程序二维码生成实例
//            try {
//                WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
//                String path = "pages/orderSubmit/orderSubmit?storeId=" + x.getStoreId() + "&roomId=" + x.getRoomId() + "&timeselectindex=0";
//                byte[] bytes = wxMaQrcodeService.createQrcodeBytes(path, 430);
//                String file = fileApi.createFile(bytes);
//                roomInfoMapper.updateById(new RoomInfoDO().setRoomId(x.getRoomId()).setQrCode(file));
//            } catch (WxErrorException e) {
////                throw new RuntimeException(e);
//            }
//        });
//        List<StoreInfoDO> storeInfoDOS = storeInfoMapper.selectList();
//        storeInfoDOS.forEach(x->{
//            // 获取小程序二维码生成实例
//            try {
//                WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
//                String path = "pages/index/index?storeId=" + x.getStoreId();
//                byte[] bytes = wxMaQrcodeService.createQrcodeBytes(path, 430);
//                String file = fileApi.createFile(bytes);
//                storeInfoMapper.updateById(new StoreInfoDO().setStoreId(x.getStoreId()).setQrCode(file));
//            } catch (WxErrorException e) {
////                throw new RuntimeException(e);
//            }
//        });
        return success("1");
    }




}

