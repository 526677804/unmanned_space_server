package com.yanzu.module.member.service.orderinfo;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.orderinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 订单管理 Service 接口
 *
 * @author 芋道源码
 */
public interface OrderInfoService {


    /**
     * 获得订单管理
     *
     * @param id 编号
     * @return 订单管理
     */
    OrderInfoDO getOrderInfo(Long id);

    /**
     * 获得订单管理分页
     *
     * @param pageReqVO 分页查询
     * @return 订单管理分页
     */
    PageResult<OrderInfoDO> getOrderInfoPage(OrderInfoPageReqVO pageReqVO);

    /**
     * 获得订单管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 订单管理列表
     */
    List<OrderInfoDO> getOrderInfoList(OrderInfoExportReqVO exportReqVO);

}
