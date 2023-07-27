package com.yanzu.module.member.dal.mysql.clearbill;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.clearbill.ClearBillDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.clearbill.vo.*;

/**
 * 保洁账单管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ClearBillMapper extends BaseMapperX<ClearBillDO> {

    default PageResult<ClearBillDO> selectPage(ClearBillPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ClearBillDO>()
//                .likeIfPresent(ClearBillDO::getUserId, reqVO.getUserId())
//                .betweenIfPresent(ClearBillDO::getMoney, reqVO.getMoney())
                .eqIfPresent(ClearBillDO::getOrderIds, reqVO.getOrderIds())
                .betweenIfPresent(ClearBillDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ClearBillDO::getId));
    }

    default List<ClearBillDO> selectList(ClearBillExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ClearBillDO>()
//                .likeIfPresent(ClearBillDO::getUserId, reqVO.getUserId())
//                .betweenIfPresent(ClearBillDO::getMoney, reqVO.getMoney())
                .eqIfPresent(ClearBillDO::getOrderIds, reqVO.getOrderIds())
                .betweenIfPresent(ClearBillDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ClearBillDO::getId));
    }

}
