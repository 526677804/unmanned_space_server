package com.yanzu.module.member.service.orderinfo;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.orderinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.orderinfo.OrderInfoConvert;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 订单管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;


    @Override
    public OrderInfoDO getOrderInfo(Long id) {
        return orderInfoMapper.selectById(id);
    }

    @Override
    public PageResult<OrderInfoDO> getOrderInfoPage(OrderInfoPageReqVO pageReqVO) {
        return orderInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OrderInfoDO> getOrderInfoList(OrderInfoExportReqVO exportReqVO) {
        return orderInfoMapper.selectList(exportReqVO);
    }

}
