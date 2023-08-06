package com.yanzu.module.member.service.payorder;

import com.yanzu.module.member.controller.app.pay.vo.PayOrderNotifyReqDTO;
import com.yanzu.module.member.controller.app.pay.vo.PayRefundNotifyReqDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import com.yanzu.module.member.controller.admin.payorder.vo.*;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.payorder.PayOrderConvert;
import com.yanzu.module.member.dal.mysql.payorder.PayOrderMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 支付订单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PayOrderServiceImpl implements PayOrderService {

    @Resource
    private PayOrderMapper payOrderMapper;

    @Override
    public PayOrderDO getPayOrder(Long id) {
        return payOrderMapper.selectById(id);
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


//        payOrderMapper.selectByOrderNoAndPayNo(notifyReqDTO);
    }

    @Override
    public void updateOrderRefunded(Map<String, String> params, String body) {

    }

    @Override
    public int create(Long userId, String orderNo, String orderDesc, Integer price, String pay_order_no) {
        return 0;
    }

}
