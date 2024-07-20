package com.yanzu.module.member.dal.mysql.groupPay;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoExportReqVO;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoPageReqVO;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoRespVO;
import com.yanzu.module.member.controller.app.chart.vo.AppChartDataReqVO;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 团购支付信息 Mapper
 *
 * @author MrGuan
 */
@Mapper
public interface GroupPayInfoMapper extends BaseMapperX<GroupPayInfoDO> {

    default PageResult<GroupPayInfoDO> selectPage(GroupPayInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<GroupPayInfoDO>()
                .likeIfPresent(GroupPayInfoDO::getGroupName, reqVO.getGroupName())
                .likeIfPresent(GroupPayInfoDO::getGroupNo, reqVO.getGroupNo())
//                .betweenIfPresent(GroupPayInfoDO::getGroupPayPrice, reqVO.getGroupPayPrice())
                .eqIfPresent(GroupPayInfoDO::getGroupPayType, reqVO.getGroupPayType())
                .eqIfPresent(GroupPayInfoDO::getStoreId, reqVO.getStoreId())
                .betweenIfPresent(GroupPayInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(GroupPayInfoDO::getId));
    }

    default List<GroupPayInfoDO> selectList(GroupPayInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<GroupPayInfoDO>()
                .likeIfPresent(GroupPayInfoDO::getGroupName, reqVO.getGroupName())
                .likeIfPresent(GroupPayInfoDO::getGroupNo, reqVO.getGroupNo())
//                .betweenIfPresent(GroupPayInfoDO::getGroupPayPrice, reqVO.getGroupPayPrice())
                .eqIfPresent(GroupPayInfoDO::getGroupPayType, reqVO.getGroupPayType())
                .eqIfPresent(GroupPayInfoDO::getStoreId, reqVO.getStoreId())
                .betweenIfPresent(GroupPayInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(GroupPayInfoDO::getId));
    }

    BigDecimal getGroupTotalMoney(@Param("storeIds") List<String> storeIds, @Param("storeId") Long storeId);

    BigDecimal getBusinessStatistics(AppChartDataReqVO reqVO);

    GroupPayInfoDO getByOrderId(Long orderId);

    IPage<GroupPayInfoRespVO> getPage(@Param("page") IPage page, @Param("reqVO") GroupPayInfoPageReqVO pageReqVO);

    BigDecimal getMtTotalMoney(@Param("storeIds") List<String> storeIds, @Param("storeId") Long storeId);

    BigDecimal getDyTotalMoney(@Param("storeIds") List<String> storeIds, @Param("storeId") Long storeId);

    BigDecimal getGroupBusinessStatistics(AppChartDataReqVO reqVO);

}
