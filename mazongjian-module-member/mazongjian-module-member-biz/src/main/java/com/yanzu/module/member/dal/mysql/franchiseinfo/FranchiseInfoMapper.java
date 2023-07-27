package com.yanzu.module.member.dal.mysql.franchiseinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.*;

/**
 * 加盟信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FranchiseInfoMapper extends BaseMapperX<FranchiseInfoDO> {

    default PageResult<FranchiseInfoDO> selectPage(FranchiseInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FranchiseInfoDO>()
                .eqIfPresent(FranchiseInfoDO::getUserId, reqVO.getUserId())
                .eqIfPresent(FranchiseInfoDO::getCity, reqVO.getCity())
                .likeIfPresent(FranchiseInfoDO::getContactName, reqVO.getContactName())
                .eqIfPresent(FranchiseInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(FranchiseInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FranchiseInfoDO::getId));
    }

    default List<FranchiseInfoDO> selectList(FranchiseInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<FranchiseInfoDO>()
                .eqIfPresent(FranchiseInfoDO::getUserId, reqVO.getUserId())
                .eqIfPresent(FranchiseInfoDO::getCity, reqVO.getCity())
                .likeIfPresent(FranchiseInfoDO::getContactName, reqVO.getContactName())
                .eqIfPresent(FranchiseInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(FranchiseInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FranchiseInfoDO::getId));
    }

}
