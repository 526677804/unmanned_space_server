package com.yanzu.module.member.controller.app.user;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.user.vo.*;
import com.yanzu.module.member.dal.dataobject.storemeituaninfo.StoreMeituanInfoDO;
import com.yanzu.module.member.dal.mysql.storemeituaninfo.StoreMeituanInfoMapper;
import com.yanzu.module.member.service.iot.MyWebSocketClient;
import com.yanzu.module.member.service.meituan.MeituanService;
import com.yanzu.module.member.service.user.AppUserService;
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
    private MyWebSocketClient myWebSocketClient;

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

//    @Autowired
//    MyWxPayService myWxPayService;

//    @Autowired
//    EwlService ewlService;

//    @Autowired
//    MemberUserApi memberUserApi;

//    @Resource
//    private IotService iotService;


    @GetMapping("/test")
    @Operation(summary = "test")
    public CommonResult<String> test() throws Exception {
//        ewlService.getThing();
//        memberUserApi.executeWxPaySplit();
//        iotService.configYunlaba("W70F9783A44");
//        iotService.runYunlaba("W70F9783A44", "尊敬的顾客您好,根据城市管理条例要求,请您在深夜消费时,注意控制噪音,以免影响到他人,感谢您的支持与理解！");
//        String authUrl = ewlService.getAuthUrl();

//        ewlService.getToken("9be670fa-026c-4595-b7c8-2871557ed372","state");
//        ewlService.getThing();
//        ewlService.login();
//        EwlSwitchReqVO reqVO = new EwlSwitchReqVO();
//        reqVO.setDeviceid("1001fbe910");
//        reqVO.setApikey("f3bfc723-56e6-4f98-a69f-1a1ab94f3e87");
//        JSONObject param = new JSONObject();
//        param.put("switch", "on");
//        reqVO.setParams(param);

       /* JSONObject data=new JSONObject();
        data.put("action","update");
        data.put("deviceid","1001fbe910");
        data.put("apikey","f3bfc723-56e6-4f98-a69f-1a1ab94f3e87");
        data.put("userAgent","app");
        data.put("sequence",new Date().getTime()+"");
        JSONObject params = new JSONObject();
        JSONArray switches=new JSONArray();
        JSONObject v=new JSONObject();
        v.put("switch", "off");
        v.put("outlet", 0);
        switches.add(v);
        params.put("switches",switches);
        data.put("params",params);
        myWebSocketClient.sendToServer(JSON.toJSONString(data));*/
//
//        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
//        refundRequest.setOutTradeNo("2024010504000603");
//        refundRequest.setOutRefundNo("2024010504000603");
//        refundRequest.setTotalFee(6990);
//        refundRequest.setRefundFee(6990);
//        WxPayService wxPayService = myWxPayService.init(5L);
//        wxPayService.refundV2(refundRequest);
        return success("1");
    }


}

