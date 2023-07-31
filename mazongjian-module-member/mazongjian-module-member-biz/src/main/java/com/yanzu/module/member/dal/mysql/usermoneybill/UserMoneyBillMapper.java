package com.yanzu.module.member.dal.mysql.usermoneybill;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.chart.vo.AppChartDataReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppUserMoneyBillPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppUserMoneyBillRespVO;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户账单明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface UserMoneyBillMapper extends BaseMapperX<UserMoneyBillDO> {


    List<AppUserMoneyBillRespVO> getOrderPage(AppUserMoneyBillPageReqVO reqVO);

    List<UserMoneyBillDO> getPayByOrderNo(@Param("orderNo") String orderNo, @Param("userId") Long userId);

}
