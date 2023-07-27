package com.yanzu.module.member.dal.mysql.orderinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.orderinfo.vo.*;

/**
 * 订单管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrderInfoMapper extends BaseMapperX<OrderInfoDO> {

    default PageResult<OrderInfoDO> selectPage(OrderInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderInfoDO>()
                .likeIfPresent(OrderInfoDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(OrderInfoDO::getRoomId, reqVO.getRoomId())
                .eqIfPresent(OrderInfoDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(OrderInfoDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(OrderInfoDO::getEndTime, reqVO.getEndTime())
//                .betweenIfPresent(OrderInfoDO::getPrice, reqVO.getPrice())
                .eqIfPresent(OrderInfoDO::getPayType, reqVO.getPayType())
                .likeIfPresent(OrderInfoDO::getGroupPayNo, reqVO.getGroupPayNo())
                .eqIfPresent(OrderInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(OrderInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(OrderInfoDO::getId)
        );
    }

    default List<OrderInfoDO> selectList(OrderInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<OrderInfoDO>()
                .likeIfPresent(OrderInfoDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(OrderInfoDO::getRoomId, reqVO.getRoomId())
                .eqIfPresent(OrderInfoDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(OrderInfoDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(OrderInfoDO::getEndTime, reqVO.getEndTime())
//                .betweenIfPresent(OrderInfoDO::getPrice, reqVO.getPrice())
                .eqIfPresent(OrderInfoDO::getPayType, reqVO.getPayType())
                .likeIfPresent(OrderInfoDO::getGroupPayNo, reqVO.getGroupPayNo())
                .eqIfPresent(OrderInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(OrderInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(OrderInfoDO::getId)
        );
    }

}
