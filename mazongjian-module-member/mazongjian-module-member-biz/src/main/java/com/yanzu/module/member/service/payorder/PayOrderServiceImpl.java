package com.yanzu.module.member.service.payorder;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yanzu.framework.common.exception.ServiceException;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.tenant.core.util.TenantUtils;
import com.yanzu.module.member.controller.admin.payorder.vo.PayOrderExportReqVO;
import com.yanzu.module.member.controller.admin.payorder.vo.PayOrderPageReqVO;
import com.yanzu.module.member.controller.app.order.vo.OrderRenewalReqVO;
import com.yanzu.module.member.controller.app.order.vo.OrderSaveReqVO;
import com.yanzu.module.member.controller.app.order.vo.WxPayOrderInfo;
import com.yanzu.module.member.controller.app.pkg.vo.AppBuyPkgReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppRechargeBalanceReqVO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import com.yanzu.module.member.dal.mysql.payorder.PayOrderMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.enums.AppWxPayTypeEnum;
import com.yanzu.module.member.service.order.AppOrderService;
import com.yanzu.module.member.service.pkg.PkgService;
import com.yanzu.module.member.service.user.AppUserService;
import com.yanzu.module.member.service.wx.MyWxService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.AppEnum.WX_PAY_ORDER;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 支付订单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class PayOrderServiceImpl implements PayOrderService {

    @Resource
    private PayOrderMapper payOrderMapper;

    @Autowired
    private MyWxService myWxService;
    @Resource
    @Lazy // 延迟，避免循环依赖报错
    private AppUserService appUserService;
    @Resource
    @Lazy // 延迟，避免循环依赖报错
    private AppOrderService appOrderService;

    @Resource
    @Lazy // 延迟，避免循环依赖报错
    private PkgService pkgService;

    @Resource
    private StoreInfoMapper storeInfoMapper;


    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public PayOrderDO getPayOrder(Long id) {
        return payOrderMapper.selectById(id);
    }

    @Override
    public PayOrderDO getPayOrderByPayNo(String orderNo) {
        return payOrderMapper.getByPayNo(orderNo);
    }

    @Override
    public List<PayOrderDO> getPayOrderList(Collection<Long> ids) {
        return payOrderMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<PayOrderDO> getPayOrderPage(PayOrderPageReqVO pageReqVO) {
        //只能查询自己门店的
//        Long tenantId = TenantContextHolder.getTenantId();
        List<StoreInfoDO> storeInfoDOS = storeInfoMapper.selectList();
        if (!CollectionUtils.isEmpty(storeInfoDOS)) {
            pageReqVO.setStoreIds(storeInfoDOS.stream().map(x -> x.getStoreId()).collect(Collectors.toList()));
            return payOrderMapper.selectPage(pageReqVO);
        } else {
            return PageResult.empty();
        }

    }

    @Override
    public List<PayOrderDO> getPayOrderList(PayOrderExportReqVO exportReqVO) {
        return payOrderMapper.selectList(exportReqVO);
    }

    @Override
//    @Transactional
    public String updateOrder(String xmlData) {
        log.info("收到微信支付回调body：{}", xmlData);
        WxPayOrderNotifyResult result = WxPayOrderNotifyResult.fromXML(xmlData);
        // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
        String orderNo = result.getOutTradeNo();
        PayOrderDO payOrderDO = payOrderMapper.getByOrderNo(orderNo);
        Long tenantId = null;
        try {
//            WxPayOrderNotifyResult result = new WxPayServiceImpl().parseOrderNotifyResult(xmlData);
            if (!ObjectUtils.isEmpty(payOrderDO)) {
                //从redis中取出
                String redisKey = String.format(WX_PAY_ORDER, orderNo);
                if (redisTemplate.hasKey(redisKey)) {
                    WxPayOrderInfo wxPayOrderInfo = (WxPayOrderInfo) redisTemplate.opsForValue().get(redisKey);
                    tenantId = wxPayOrderInfo.getTenantId();
                    //模拟租户 处理支付订单
                    TenantUtils.execute(tenantId, () -> {
                        if (wxPayOrderInfo.getWxPayTypeEnum().compareTo(AppWxPayTypeEnum.ORDER) == 0) {
                            //下单
                            OrderSaveReqVO reqVO = new OrderSaveReqVO();
                            reqVO.setOrderNo(orderNo);
                            reqVO.setPayType(payOrderDO.getPayType());
                            reqVO.setCouponId(wxPayOrderInfo.getCouponId());
                            reqVO.setNightLong(wxPayOrderInfo.getNightLong());
                            reqVO.setRoomId(wxPayOrderInfo.getRoomId());
                            reqVO.setStartTime(wxPayOrderInfo.getStartTime());
                            reqVO.setEndTime(wxPayOrderInfo.getEndTime());
                            reqVO.setUserId(wxPayOrderInfo.getUserId());
                            reqVO.setPkgId(wxPayOrderInfo.getPkgId());
                            appOrderService.save(reqVO);
                        } else if (wxPayOrderInfo.getWxPayTypeEnum().compareTo(AppWxPayTypeEnum.RENEW) == 0) {
                            //续费
                            OrderRenewalReqVO orderRenewalReqVO = new OrderRenewalReqVO();
                            orderRenewalReqVO.setOrderId(wxPayOrderInfo.getIgnoreOrderId());
                            orderRenewalReqVO.setOrderNo(wxPayOrderInfo.getOrderNo());
                            orderRenewalReqVO.setEndTime(wxPayOrderInfo.getEndTime());
                            orderRenewalReqVO.setPayType(AppEnum.order_pay_type.WEIXIN.getValue());
                            orderRenewalReqVO.setUserId(wxPayOrderInfo.getUserId());
                            orderRenewalReqVO.setPkgId(wxPayOrderInfo.getPkgId());
                            orderRenewalReqVO.setCouponId(wxPayOrderInfo.getCouponId());
                            appOrderService.renew(orderRenewalReqVO);
                        } else if (wxPayOrderInfo.getWxPayTypeEnum().compareTo(AppWxPayTypeEnum.RECHARGE) == 0) {
                            //充值
                            AppRechargeBalanceReqVO rechargeBalanceReqVO = new AppRechargeBalanceReqVO();
                            rechargeBalanceReqVO.setUserId(wxPayOrderInfo.getUserId());
                            rechargeBalanceReqVO.setOrderNo(orderNo);
                            rechargeBalanceReqVO.setStoreId(wxPayOrderInfo.getStoreId());
                            rechargeBalanceReqVO.setPrice(payOrderDO.getPrice());
                            appUserService.eechargeBalance(rechargeBalanceReqVO);
                        } else if (wxPayOrderInfo.getWxPayTypeEnum().compareTo(AppWxPayTypeEnum.PKG) == 0) {
                            //购买套餐
                            AppBuyPkgReqVO reqVO = new AppBuyPkgReqVO();
                            reqVO.setOrderNo(orderNo);
                            reqVO.setUserId(wxPayOrderInfo.getUserId());
                            reqVO.setPkgId(wxPayOrderInfo.getPkgId());
                            reqVO.setPrice(payOrderDO.getPrice());
                            pkgService.buyPkg(reqVO);
                        }
                    });
                }
            }
            return WxPayNotifyResponse.success("接收成功!");
        } catch (ServiceException e) {
            e.printStackTrace();
            //模拟租户 处理支付订单
            TenantUtils.execute(tenantId, () -> {
                //业务异常 退款
                WxPayRefundRequest refundRequest = new WxPayRefundRequest();
                refundRequest.setOutTradeNo(payOrderDO.getOrderNo());
                refundRequest.setOutRefundNo("TK" + payOrderDO.getOrderNo());
                refundRequest.setTotalFee(payOrderDO.getPrice());
                refundRequest.setRefundFee(payOrderDO.getPrice());
                refundRequest.setRefundDesc("订单支付失败退款");
                WxPayService wxPayService = myWxService.initWxPay(payOrderDO.getStoreId());
                try {
                    wxPayService.refundV2(refundRequest);
                } catch (WxPayException ex) {
//                throw new RuntimeException(ex);
                    log.error("微信支付订单退款失败:{}", orderNo);
                }
                payOrderDO.setPayStatus(true);
                payOrderDO.setPayRefundNo(refundRequest.getOutRefundNo());
                payOrderDO.setRefundPrice(payOrderDO.getPrice());
                payOrderMapper.updateById(payOrderDO);
            });
            return WxPayNotifyResponse.success("接收成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
//        payOrderMapper.selectByOrderNoAndPayNo(notifyReqDTO);
    }

    @Override
    public String updateOrderRefunded(Map<String, String> params, String body) {
        log.info("收到微信支付退款回调body：{}", body);
        log.info("收到微信支付退款回调params：{}", params);


        return WxPayNotifyResponse.success("接收成功!");
    }

    @Override
    @Transactional
    public void create(Long userId, String orderNo, Long orderId, Long storeId, Integer payType, String orderDesc, Integer price) {
        PayOrderDO payOrderDO = new PayOrderDO();
        payOrderDO.setUserId(userId);
        payOrderDO.setOrderNo(orderNo);
        payOrderDO.setOrderId(orderId);
        payOrderDO.setStoreId(storeId);
        payOrderDO.setPayType(payType);
        payOrderDO.setOrderDesc(orderDesc);
        payOrderDO.setPrice(price);
        payOrderMapper.insert(payOrderDO);
    }

    @Override
    public PayOrderDO getByOrderNo(String orderNo) {
        return payOrderMapper.getByOrderNo(orderNo);
    }

    @SneakyThrows
    public void checkWxOrder(String orderNo, Long storeId, Integer price) {
        log.info("检查门店:{}，订单：{}，微信支付状态！", storeId, orderNo);
        String redisKey = String.format(WX_PAY_ORDER, orderNo);
        if (redisTemplate.hasKey(redisKey)) {
            //如果已经验证了 就移除这个订单号
            redisTemplate.delete(redisKey);
            //再验证支付是否成功
            PayOrderDO payOrderDO = getByOrderNo(orderNo);
            if (ObjectUtils.isEmpty(payOrderDO)) {
                throw exception(ORDER_WEIXIN_PAY_ERROR);
            }
            //创建微信支付实例
            WxPayService wxPayService = myWxService.initWxPay(storeId);
            WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(null, orderNo);
            String tradeNo = wxPayOrderQueryResult.getTransactionId();
            String tradeState = wxPayOrderQueryResult.getTradeState();
            String returnCode = wxPayOrderQueryResult.getReturnCode();
            Integer cashFee = wxPayOrderQueryResult.getTotalFee();//支付金额
            String resultCode = wxPayOrderQueryResult.getResultCode();
            log.info("tradeState:{},returnCode:{},resultCode:{},", tradeState, returnCode, resultCode);
            //判断支付结果
            boolean flag = tradeState.equals("SUCCESS") && returnCode.equals("SUCCESS") && resultCode.equals("SUCCESS");
            //对比实际支付的价格 和订单应支付的价格是否一致
            boolean checkPrice = cashFee >= price;
            log.info("订单：{}，微信支付状态为：{},price:{},cashFee:{}", orderNo, flag, price, cashFee);
            if (flag && checkPrice) {
                String transactionId = wxPayOrderQueryResult.getTransactionId();//微信支付订单号
//                    String outTradeNo = wxPayOrderQueryResult.getOutTradeNo();//商家订单号
                //更新支付订单状态
                if (!payOrderDO.getPayStatus()) {
                    //更新状态、支付订单号 和支付金额
                    payOrderDO.setPrice(cashFee);
                    payOrderDO.setPayOrderNo(tradeNo);
                    payOrderDO.setPayOrderNo(transactionId);
                    payOrderDO.setPayStatus(true);
                    payOrderDO.setPayTime(LocalDateTime.now());
                    payOrderMapper.updateById(payOrderDO);
                }
            } else {
                throw exception(ORDER_WEIXIN_PAY_ERROR);
            }
        } else {
            throw exception(ORDER_WEIXIN_PAY_ERROR);
        }

    }

    @Override
    @Transactional
    public void refundOrder(Long id) {
        //查询出订单
        PayOrderDO payOrderDO = payOrderMapper.selectById(id);
        if (!ObjectUtils.isEmpty(payOrderDO)) {
            if (payOrderDO.getPayStatus() && payOrderDO.getRefundPrice() == 0) {
                WxPayRefundRequest refundRequest = new WxPayRefundRequest();
                refundRequest.setOutTradeNo(payOrderDO.getOrderNo());
                refundRequest.setOutRefundNo("TK" + payOrderDO.getOrderNo());
                refundRequest.setTotalFee(payOrderDO.getPrice());
                refundRequest.setRefundFee(payOrderDO.getPrice());
                refundRequest.setRefundDesc("管理员退款");
                WxPayService wxPayService = myWxService.initWxPay(payOrderDO.getStoreId());
                try {
                    wxPayService.refundV2(refundRequest);
                    payOrderDO.setPayRefundNo(refundRequest.getOutRefundNo());
                    payOrderDO.setRefundPrice(payOrderDO.getPrice());
                    payOrderDO.setRefundTime(LocalDateTime.now());
                    payOrderMapper.updateById(payOrderDO);
                } catch (WxPayException ex) {
//                throw new RuntimeException(ex);
//                    log.error("微信支付订单:{}，退款失败！", orderNo);
                }

            } else {
                throw exception(ADMIN_WEIXIN_PAY_REFOUND_ERROR);
            }
        }

    }

    /**
     * @param orderNo 订单号
     * @param price   金额 单位分
     */
    @Override
    @Transactional
    public void refundDeposit(String orderNo, int price) {
        //查询出订单
        PayOrderDO payOrderDO = payOrderMapper.getByOrderNo(orderNo);
        if (!ObjectUtils.isEmpty(payOrderDO)) {
            if (payOrderDO.getPayStatus() && payOrderDO.getRefundPrice() == 0) {
                WxPayRefundRequest refundRequest = new WxPayRefundRequest();
                refundRequest.setOutTradeNo(payOrderDO.getOrderNo());
                refundRequest.setOutRefundNo("TK" + payOrderDO.getOrderNo());
                refundRequest.setTotalFee(payOrderDO.getPrice());
                refundRequest.setRefundFee(price);
                refundRequest.setRefundDesc("押金退款");
                WxPayService wxPayService = myWxService.initWxPay(payOrderDO.getStoreId());
                try {
                    wxPayService.refundV2(refundRequest);
                    payOrderDO.setPayRefundNo(refundRequest.getOutRefundNo());
                    payOrderDO.setRefundPrice(payOrderDO.getPrice());
                    payOrderDO.setRefundTime(LocalDateTime.now());
                    payOrderMapper.updateById(payOrderDO);
                } catch (WxPayException ex) {
//                throw new RuntimeException(ex);
//                    log.error("微信支付订单:{}，退款失败！", orderNo);
                }
            } else {
                throw exception(ADMIN_WEIXIN_PAY_REFOUND_ERROR);
            }
        }

    }

    @Resource
    private StoreUserMapper storeUserMapper;

    @Resource
    private UserMoneyBillMapper userMoneyBillMapper;

    /**
     * 余额退款
     *
     * @param orderNo
     * @param userId
     */
    @Override
    public void refundBalance(Long storeId, String orderNo, Long userId) {
        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(userId, storeId);
        //余额退款  把支付记录找出来
        List<UserMoneyBillDO> userMoneyBillDOList = userMoneyBillMapper.getPayByOrderNo(orderNo, userId);
        if (!CollectionUtils.isEmpty(userMoneyBillDOList)) {
            for (UserMoneyBillDO billDO : userMoneyBillDOList) {
                UserMoneyBillDO newUserMoneyBillDO = new UserMoneyBillDO();
                BeanUtils.copyProperties(billDO, newUserMoneyBillDO);
                newUserMoneyBillDO.setId(null);
                newUserMoneyBillDO.setCreateTime(null);
                newUserMoneyBillDO.setCreator(null);
                newUserMoneyBillDO.setUpdateTime(null);
                newUserMoneyBillDO.setUpdater(null);
                newUserMoneyBillDO.setType(AppEnum.user_money_bill_type.REFUND.getValue());//改成退款状态
                newUserMoneyBillDO.setRemark(newUserMoneyBillDO.getRemark().replace("支付", "退款"));
                if (billDO.getMoneyType().intValue() == 1) {
                    //账户余额  加回去
                    storeUserDO.setBalance(storeUserDO.getBalance().add(billDO.getMoney()));
                    synchronized (this) {
                        storeUserMapper.updateById(storeUserDO);
                    }
                    newUserMoneyBillDO.setTotalMoney(storeUserDO.getBalance());
                } else if (billDO.getMoneyType().intValue() == 2) {
                    //赠送余额  加回去
                    storeUserDO.setGiftBalance(storeUserDO.getGiftBalance().add(billDO.getMoney()));
                    synchronized (this) {
                        storeUserMapper.updateById(storeUserDO);
                    }
                    newUserMoneyBillDO.setTotalGiftMoney(storeUserDO.getGiftBalance());
                } else {
                    throw exception(OPRATION_ERROR);
                }
                userMoneyBillMapper.insert(newUserMoneyBillDO);
            }
        }

    }

    @Override
    public void refundByOrder(Long orderId, String orderNo, Long storeId) {
        List<PayOrderDO> payOrderList = payOrderMapper.getByOrder(orderId,orderNo);
        if (!CollectionUtils.isEmpty(payOrderList)) {
            WxPayService wxPayService = myWxService.initWxPay(storeId);
            //退款
            for (PayOrderDO payOrderDO : payOrderList) {
                WxPayRefundRequest refundRequest = new WxPayRefundRequest();
                refundRequest.setOutTradeNo(payOrderDO.getOrderNo());
                refundRequest.setOutRefundNo("TK" + payOrderDO.getOrderNo());
                refundRequest.setTotalFee(payOrderDO.getPrice());
                refundRequest.setRefundFee(payOrderDO.getPrice());
                refundRequest.setRefundDesc("退款");
                try {
                    wxPayService.refundV2(refundRequest);
                    payOrderDO.setPayRefundNo(refundRequest.getOutRefundNo());
                    payOrderDO.setRefundPrice(payOrderDO.getPrice());
                    payOrderDO.setRefundTime(LocalDateTime.now());
                    payOrderMapper.updateById(payOrderDO);
                } catch (WxPayException ex) {
//                throw new RuntimeException(ex);
                    log.error("微信支付订单:{}，退款失败！原因：{}", payOrderDO.getOrderNo(), ex.getMessage());
                }
            }
        }


    }

}
