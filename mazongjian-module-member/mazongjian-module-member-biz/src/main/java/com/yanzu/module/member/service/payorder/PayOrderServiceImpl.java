package com.yanzu.module.member.service.payorder;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.payorder.vo.PayOrderExportReqVO;
import com.yanzu.module.member.controller.admin.payorder.vo.PayOrderPageReqVO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.module.member.dal.mysql.payorder.PayOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
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
    public void updateOrder(Map<String, String> params, String body) {

        log.info("收到微信支付回调:{},body{}", params, body);
//        payOrderMapper.selectByOrderNoAndPayNo(notifyReqDTO);
    }

    @Override
    public void updateOrderRefunded(Map<String, String> params, String body) {
        log.info("收到微信支付回调:{},body{}", params, body);
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
