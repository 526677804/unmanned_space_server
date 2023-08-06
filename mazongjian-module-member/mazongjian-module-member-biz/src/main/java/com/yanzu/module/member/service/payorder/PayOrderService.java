package com.yanzu.module.member.service.payorder;

import java.util.*;
import javax.validation.*;

import com.yanzu.module.member.controller.admin.payorder.vo.*;
import com.yanzu.module.member.controller.app.pay.vo.PayOrderNotifyReqDTO;
import com.yanzu.module.member.controller.app.pay.vo.PayRefundNotifyReqDTO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 支付订单 Service 接口
 *
 * @author 芋道源码
 */
public interface PayOrderService {


    /**
     * 获得支付订单
     *
     * @param id 编号
     * @return 支付订单
     */
    PayOrderDO getPayOrder(Long id);

    /**
     * 获得支付订单列表
     *
     * @param ids 编号
     * @return 支付订单列表
     */
    List<PayOrderDO> getPayOrderList(Collection<Long> ids);

    /**
     * 获得支付订单分页
     *
     * @param pageReqVO 分页查询
     * @return 支付订单分页
     */
    PageResult<PayOrderDO> getPayOrderPage(PayOrderPageReqVO pageReqVO);

    /**
     * 获得支付订单列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 支付订单列表
     */
    List<PayOrderDO> getPayOrderList(PayOrderExportReqVO exportReqVO);


    void updateOrder(Map<String, String> params, String body);

    void updateOrderRefunded(Map<String, String> params, String body);

    int create(Long userId, String orderNo, String orderDesc, Integer price, String pay_order_no);
}
