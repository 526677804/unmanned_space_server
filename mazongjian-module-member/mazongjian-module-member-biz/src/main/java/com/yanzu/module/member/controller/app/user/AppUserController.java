package com.yanzu.module.member.controller.app.user;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderRespVO;
import com.yanzu.module.member.controller.app.user.vo.*;
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
//    private MeituanClient meituanClient;
//    @Autowired
//    WxPayService wxPayService;

//    @GetMapping("/test")
//    @Operation(summary = "test")
//    public CommonResult<Boolean> test() throws Exception {
//        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
//        refundRequest.setOutTradeNo("2023092805192951");
//        refundRequest.setOutRefundNo("TK2023092805192951" );
//        refundRequest.setTotalFee(1000);
//        refundRequest.setRefundFee(1000);
//        wxPayService.refund(refundRequest);
//        return success(true);
//    }
//  /*      MeituanPrepareReqVO reqVO = new MeituanPrepareReqVO();
//        reqVO.setApp_key("022008863ebef333");
//        reqVO.setOpen_shop_uuid("6eb50f3547e1195d43eb447b8ab62449");
//        reqVO.setSession("cc7a13ae967015e42ae1cd6484f3663766e1f09d");
//        reqVO.setReceipt_code("5571630793");
//        Map<String, String> paramMap = MeituanSignUtils.convertBeanToMap(reqVO);
//        String sign = MeituanSignUtils.generateSign(paramMap, "8c7729556cb497fcec48133dc760dd2808ba1399", MeituanConstants.SIGN_METHOD_MD5);
//        reqVO.setSign(sign);
//        JSONObject prepare = meituanClient.prepare(reqVO);
//        log.info("prepare:{}", prepare);*/
//
//       /* MeituanConsumeReqVO reqVO=new MeituanConsumeReqVO();
//        reqVO.setApp_key("022008863ebef333");
//        reqVO.setOpen_shop_uuid("6eb50f3547e1195d43eb447b8ab62449");
//        reqVO.setSession("cc7a13ae967015e42ae1cd6484f3663766e1f09d");
//        reqVO.setReceipt_code("5571630793");
//        reqVO.setApp_shop_account("1");
//        reqVO.setApp_shop_accountname("user");
//        Map<String, String> paramMap = MeituanSignUtils.convertBeanToMap(reqVO);
//        String sign = MeituanSignUtils.generateSign(paramMap, "8c7729556cb497fcec48133dc760dd2808ba1399", MeituanConstants.SIGN_METHOD_MD5);
//        reqVO.setSign(sign);
//        JSONObject consume = meituanClient.consume(reqVO);
//        log.info("consume:{}", consume);*/
//
//        MeituanReverseconsumeReqVO reqVO=new MeituanReverseconsumeReqVO();
//        reqVO.setApp_key("022008863ebef333");
//        reqVO.setOpen_shop_uuid("6eb50f3547e1195d43eb447b8ab62449");
//        reqVO.setSession("cc7a13ae967015e42ae1cd6484f3663766e1f09d");
//        reqVO.setReceipt_code("5571630793");
//        reqVO.setApp_deal_id("993507556");
//        reqVO.setApp_shop_account("1");
//        reqVO.setApp_shop_accountname("user");
//        Map<String, String> paramMap = MeituanSignUtils.convertBeanToMap(reqVO);
//        String sign = MeituanSignUtils.generateSign(paramMap, "8c7729556cb497fcec48133dc760dd2808ba1399", MeituanConstants.SIGN_METHOD_MD5);
//        reqVO.setSign(sign);
//        JSONObject reverseconsume = meituanClient.reverseconsume(reqVO);
//        log.info("reverseconsume:{}", reverseconsume);
//        return success(true);
//    }

}

