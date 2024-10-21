package com.yanzu.module.member.api.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.profitsharing.ProfitSharingFinishRequest;
import com.github.binarywang.wxpay.bean.profitsharing.ProfitSharingRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.ProfitSharingService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yanzu.module.member.api.user.dto.MemberUserRespDTO;
import com.yanzu.module.member.convert.user.UserConvert;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.couponinfo.CouponInfoMapper;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;
import com.yanzu.module.member.dal.mysql.payorder.PayOrderMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.service.order.AppOrderService;
import com.yanzu.module.member.service.user.AppUserService;
import com.yanzu.module.member.service.wx.MyWxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员用户的 API 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class MemberUserApiImpl implements MemberUserApi {

    @Resource
    private AppUserService userService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private CouponInfoMapper couponInfoMapper;

    @Resource
    private DiscountRulesMapper discountRulesMapper;

    @Resource
    private MyWxService myWxService;

    @Resource
    private PayOrderMapper payOrderMapper;

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Value("${wx.pay.splitMchId}")
    private String splitMchId;


    @Override
    public MemberUserRespDTO getUser(Long id) {
        MemberUserDO user = userService.getUser(id);
        return UserConvert.INSTANCE.convert2(user);
    }

    @Override
    public List<MemberUserRespDTO> getUsers(Collection<Long> ids) {
        return UserConvert.INSTANCE.convertList2(userService.getUserList(ids));
    }

    @Override
    public List<MemberUserRespDTO> getUserListByNickname(String nickname) {
        return UserConvert.INSTANCE.convertList2(userService.getUserListByNickname(nickname));
    }

    @Override
    public MemberUserRespDTO getUserByMobile(String mobile) {
        return UserConvert.INSTANCE.convert2(userService.getUserByMobile(mobile));
    }

    /**
     * 订单处理的定时任务，每分钟执行一次， 用于到时间开始订单 或者 结束订单
     */
    @Override
    public void executeOrderJob() {
        appOrderService.executeOrderJob();

    }

    @Override
    public void executeMeituanRefreshTokenJob() {
        appOrderService.executeMeituanRefreshTokenJob();
    }

    @Override
    @Transactional
    public void executeCouponExpire() {
        log.info("==========     开始执行每日定时检查任务     ==========");
        //处理过期 但是未使用的优惠券
        couponInfoMapper.executeCouponExpire();
        //处理过期的充值优惠规则
        discountRulesMapper.executeExpire();
        //处理门店到期
        storeInfoMapper.executeExpire();
    }

    @Override
    public void executeWxPaySplit() {
        log.info("==========     开始执行微信分账定时检查任务     ==========");
        //获取今天之前的已支付但未分账的订单
        List<PayOrderDO> preSplit = payOrderMapper.getPreSplit();
        if (!CollectionUtils.isEmpty(preSplit)) {
            List<Long> splitId = new ArrayList<>(preSplit.size());
            //按storeId分组
            Map<Long, List<PayOrderDO>> listMap = preSplit.stream().collect(Collectors.groupingBy(x -> x.getStoreId()));
            for (Map.Entry<Long, List<PayOrderDO>> entry : listMap.entrySet()) {
                //查询分账比例
                StoreWxpayConfigDO config = myWxService.getWxPayConfig(entry.getKey());
                if(config.getSplit()){
                    //初始化微信支付
                    WxPayService wxPayService = myWxService.initWxPay(entry.getKey());
                    //初始化分账服务
                    ProfitSharingService profitSharingService = wxPayService.getProfitSharingService();
                    for (PayOrderDO payOrderDO : entry.getValue()) {
                        try {
                            int amount = payOrderDO.getPrice() * config.getSplitProp() / 100;
                            if (amount == 0) {
                                //直接退回资金
                                ProfitSharingFinishRequest finishRequest = new ProfitSharingFinishRequest();
                                finishRequest.setNonceStr(UUID.randomUUID().toString().substring(0, 16));
                                finishRequest.setTransactionId(payOrderDO.getPayOrderNo());
                                finishRequest.setOutOrderNo("P" + payOrderDO.getOrderNo());
                                finishRequest.setDescription("支付服务费");
                                profitSharingService.profitSharingFinish(finishRequest);
                            } else {
                                ProfitSharingRequest req = new ProfitSharingRequest();
                                req.setNonceStr(UUID.randomUUID().toString().substring(0, 16));
                                req.setTransactionId(payOrderDO.getPayOrderNo());
                                req.setOutOrderNo("P" + payOrderDO.getOrderNo());
                                JSONArray jsonArr = new JSONArray();
                                JSONObject json = new JSONObject();
                                json.put("type", "MERCHANT_ID");
                                json.put("account", splitMchId);
                                json.put("amount", payOrderDO.getPrice() * config.getSplitProp() / 100);
                                json.put("description", "支付服务费");
                                jsonArr.add(json);
                                req.setReceivers(jsonArr.toJSONString());
                                profitSharingService.profitSharing(req);
                            }
                            splitId.add(payOrderDO.getId());
                        } catch (WxPayException e) {
                            log.error("微信支付分账失败:{}", payOrderDO.getId());
                            e.printStackTrace();
//                        throw new RuntimeException(e);
                            continue;
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(splitId)) {
                payOrderMapper.finishSplit(splitId);
            }
        }
    }

}
