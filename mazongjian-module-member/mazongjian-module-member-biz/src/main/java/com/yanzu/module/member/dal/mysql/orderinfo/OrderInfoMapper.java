package com.yanzu.module.member.dal.mysql.orderinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.chart.vo.AppChartDataReqVO;
import com.yanzu.module.member.controller.app.chart.vo.AppIncomeStatisticsRespVO;
import com.yanzu.module.member.controller.app.chart.vo.AppRechargeStatisticsRespVO;
import com.yanzu.module.member.controller.app.clear.vo.AppClearPageReqVO;
import com.yanzu.module.member.controller.app.clear.vo.AppClearPageRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderInfoAppRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderListJobVO;
import com.yanzu.module.member.controller.app.order.vo.OrderListRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderPageReqVO;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
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

    IPage<AppClearPageRespVO> getClearPage(@Param("page") IPage<AppClearPageRespVO> page, @Param("reqVO") AppClearPageReqVO reqVO);

    List<OrderInfoDO> getByRoomId(@Param("roomId") Long roomId, @Param("ignoreOrderId") Long ignoreOrderId);
    int countByRoomId(@Param("roomId") Long roomId, @Param("ignoreOrderId") Long ignoreOrderId);

    IPage<OrderListRespVO> getOrderPage(@Param("page") IPage page, @Param("reqVO") OrderPageReqVO reqVO);

    OrderInfoAppRespVO getOrderInfo(@Param("orderId") Long orderId,@Param("orderKey") String orderKey, @Param("userId") Long userId);

    List<KeyValue<String, BigDecimal>> getRevenueStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, Integer>> getOrderStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, Integer>> getMemberStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, Integer>> getNewMemberStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, Long>> getRoomUseStatistics(AppChartDataReqVO reqVO);

    List<KeyValue<String, String>> getRoomUseHourStatistics(AppChartDataReqVO reqVO);

    List<OrderInfoDO> getByStatus(Integer status);
    List<OrderListJobVO> getListByJob();

    int updateStatusByIds(@Param("status") Integer status, @Param("orderIds") String orderIds);

    Integer countByRoomIdGtNow(Long roomId);

    OrderInfoDO getByOrderNo(String orderNo);


    OrderInfoDO getByRoomCurrent(Long roomId);

    int countByRoomCurrent(@Param("roomId") Long roomId, @Param("ignoreOrderId") Long ignoreOrderId);

    Integer getWxTotalMoney(@Param("storeIds") List<String> storeIds, @Param("storeId") Long storeId);

    Integer countByStoreIds(@Param("storeIds") List<String> storeIds, @Param("storeId") Long storeId);

    int changeOrderUser(@Param("orderId") Long orderId, @Param("userId") Long userId);

    Integer getCountOrder(AppChartDataReqVO reqVO);

    Integer countByPreOrder(@Param("roomId") Long roomId, @Param("clearTime") Integer clearTime,
                            @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("ignoreOrderId") Long ignoreOrderId);

    int countByUserAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);

    Integer countUser(AppChartDataReqVO reqVO);

    List<AppIncomeStatisticsRespVO> getIncomeStatistics(AppChartDataReqVO reqVO);

    List<AppRechargeStatisticsRespVO> getRechargeStatistics(AppChartDataReqVO reqVO);

    OrderInfoDO getRepeatOrder(@Param("roomId") Long roomId, @Param("startTime") Date startTime);

    int countNewUserByStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);
}
