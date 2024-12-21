package com.yanzu.module.member.service.orderinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;


import com.yanzu.module.member.controller.admin.orderinfo.vo.*;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;

import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;


/**
 * 订单管理 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Override
    public void deleteOrderInfo(Long id) {
        log.info("管理员：{}，删除订单：{}", getLoginUserId(), id);
        // 删除
        orderInfoMapper.deleteById(id);
    }

    @Override
    public PageResult<OrderInfoRespVO> getOrderInfoPage(OrderInfoPageReqVO reqVO) {
        IPage<OrderInfoRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        orderInfoMapper.getOrderInfoPage(page, reqVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }


}
