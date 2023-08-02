package com.yanzu.module.member.dal.mysql.orderinfo;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.chart.vo.AppBusinessStatisticsRespVO;
import com.yanzu.module.member.controller.app.chart.vo.AppChartDataReqVO;
import com.yanzu.module.member.controller.app.clear.vo.AppClearPageReqVO;
import com.yanzu.module.member.controller.app.clear.vo.AppClearPageRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderInfoAppRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderListRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderPageReqVO;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrderInfoMapper extends BaseMapperX<OrderInfoDO> {

    List<OrderInfoDO> getByRoomIds(@Param("roomIds") List<Long> roomIds);

    OrderInfoDO getByUserAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    List<AppClearPageRespVO> getClearPage(AppClearPageReqVO reqVO);

    List<OrderInfoDO> getByRoomId(@Param("roomId") Long roomId, @Param("ignoreOrderId") Long ignoreOrderId);

    List<OrderListRespVO> getOrderPage(OrderPageReqVO reqVO);

    OrderInfoAppRespVO getOrderInfo(Long orderId);

    AppBusinessStatisticsRespVO getBusinessStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, BigDecimal>> getRevenueStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, Integer>> getOrderStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, Integer>> getMemberStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, Integer>> getNewMemberStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, Long>> getRoomUseStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, Double>> getRoomUseHourStatistics(AppChartDataReqVO reqVO);

    List<OrderInfoDO> getByStatus(Integer status);
}
