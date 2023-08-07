package com.yanzu.module.member.dal.mysql.payorder;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.callback.vo.PayOrderNotifyReqDTO;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.payorder.vo.*;

/**
 * 支付订单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PayOrderMapper extends BaseMapperX<PayOrderDO> {

    default PageResult<PayOrderDO> selectPage(PayOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PayOrderDO>()
                .likeIfPresent(PayOrderDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(PayOrderDO::getPayStatus, reqVO.getPayStatus())
                .likeIfPresent(PayOrderDO::getPayOrderNo, reqVO.getPayOrderNo())
                .betweenIfPresent(PayOrderDO::getPayTime, reqVO.getPayTime())
                .likeIfPresent(PayOrderDO::getPayRefundNo, reqVO.getPayRefundNo())
                .betweenIfPresent(PayOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PayOrderDO::getId));
    }

    default List<PayOrderDO> selectList(PayOrderExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<PayOrderDO>()
                .likeIfPresent(PayOrderDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(PayOrderDO::getPayStatus, reqVO.getPayStatus())
                .likeIfPresent(PayOrderDO::getPayOrderNo, reqVO.getPayOrderNo())
                .betweenIfPresent(PayOrderDO::getPayTime, reqVO.getPayTime())
                .likeIfPresent(PayOrderDO::getPayRefundNo, reqVO.getPayRefundNo())
                .betweenIfPresent(PayOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PayOrderDO::getId));
    }

    PayOrderDO selectByOrderNoAndPayNo(PayOrderNotifyReqDTO notifyReqDTO);
}
