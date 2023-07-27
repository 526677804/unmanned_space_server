package com.yanzu.module.member.dal.mysql.clearinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.clearinfo.vo.*;

/**
 * 保洁信息管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ClearInfoMapper extends BaseMapperX<ClearInfoDO> {

    default PageResult<ClearInfoDO> selectPage(ClearInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ClearInfoDO>()
                .likeIfPresent(ClearInfoDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(ClearInfoDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(ClearInfoDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(ClearInfoDO::getSettlementTime, reqVO.getSettlementTime())
                .eqIfPresent(ClearInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ClearInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ClearInfoDO::getClearId));
    }

    default List<ClearInfoDO> selectList(ClearInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ClearInfoDO>()
                .likeIfPresent(ClearInfoDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(ClearInfoDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(ClearInfoDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(ClearInfoDO::getSettlementTime, reqVO.getSettlementTime())
                .eqIfPresent(ClearInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ClearInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ClearInfoDO::getClearId));
    }

}
