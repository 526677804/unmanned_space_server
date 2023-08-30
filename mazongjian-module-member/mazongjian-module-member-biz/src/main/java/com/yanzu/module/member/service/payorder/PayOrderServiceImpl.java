package com.yanzu.module.member.service.payorder;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.payorder.vo.PayOrderExportReqVO;
import com.yanzu.module.member.controller.admin.payorder.vo.PayOrderPageReqVO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.module.member.dal.mysql.payorder.PayOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    private WxPayService wxPayService;

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
        return payOrderMapper.selectPage(pageReqVO);
    }

    @Override
    public List<PayOrderDO> getPayOrderList(PayOrderExportReqVO exportReqVO) {
        return payOrderMapper.selectList(exportReqVO);
    }

    @Override
    @Transactional
    public String updateOrder(String xmlData) {
        log.info("收到微信支付回调body：{}", xmlData);
//        log.info("收到微信支付回调params：{}",params);
        try {
//            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlData);
            // 加入自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
            String orderNo = result.getOutTradeNo();
            PayOrderDO orderDO = payOrderMapper.getByOrderNo(orderNo);
            if (!ObjectUtils.isEmpty(orderDO) && !orderDO.getPayStatus()) {
                String totalFee = BaseWxPayResult.fenToYuan(result.getTotalFee());
                String tradeNo = result.getTransactionId();
                if (String.valueOf(orderDO.getPrice()).equals(totalFee)) {
                    return WxPayNotifyResponse.fail("实际支付金额与订单应支付金额不匹配！");
                }
                orderDO.setPayOrderNo(tradeNo);
                orderDO.setPayStatus(true);
                orderDO.setPayTime(LocalDateTime.now());
                payOrderMapper.updateById(orderDO);
            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
//        payOrderMapper.selectByOrderNoAndPayNo(notifyReqDTO);
    }

    @Override
    public String updateOrderRefunded(Map<String, String> params, String body) {
        log.info("收到微信支付回调body：{}", body);
        log.info("收到微信支付回调params：{}", params);
        return WxPayNotifyResponse.success("处理成功!");
    }

    @Override
    @Transactional
    public void create(Long userId, String orderNo, String orderDesc, Integer price) {
        PayOrderDO payOrderDO = new PayOrderDO();
        payOrderDO.setUserId(userId);
        payOrderDO.setOrderNo(orderNo);
        payOrderDO.setOrderDesc(orderDesc);
        payOrderDO.setPrice(price);
        payOrderMapper.insert(payOrderDO);
    }

    @Override
    public PayOrderDO getByOrderNo(String orderNo) {
        return payOrderMapper.getByOrderNo(orderNo);
    }

}
