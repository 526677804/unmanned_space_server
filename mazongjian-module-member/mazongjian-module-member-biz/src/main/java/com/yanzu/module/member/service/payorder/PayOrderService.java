package com.yanzu.module.member.service.payorder;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.payorder.vo.PayOrderExportReqVO;
import com.yanzu.module.member.controller.admin.payorder.vo.PayOrderPageReqVO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    PayOrderDO getPayOrderByPayNo(String orderNo);

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


    String updateOrder(String xmlData);

    String updateOrderRefunded(Map<String, String> params, String body);

    void create(Long userId, String orderNo,Long orderId, Long storeId, Integer payType, String orderDesc, Integer price);

    PayOrderDO getByOrderNo(String orderNo);

    void checkWxOrder(String orderNo,Long storeId, Integer price);

    void refundOrder(Long id);

    void refundDeposit(String orderNo, int price);

    void refundBalance(Long storeId,String orderNo,Long userId);

    void refundByOrder(Long orderId, String orderNo,Long storeId);
}
