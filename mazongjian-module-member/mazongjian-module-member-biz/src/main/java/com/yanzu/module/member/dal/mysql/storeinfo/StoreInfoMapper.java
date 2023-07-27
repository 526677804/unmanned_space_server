package com.yanzu.module.member.dal.mysql.storeinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.storeinfo.vo.*;

/**
 * 门店管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface StoreInfoMapper extends BaseMapperX<StoreInfoDO> {

    default PageResult<StoreInfoDO> selectPage(StoreInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StoreInfoDO>()
                .likeIfPresent(StoreInfoDO::getStoreName, reqVO.getStoreName())
                .likeIfPresent(StoreInfoDO::getCityName, reqVO.getCityName())
                .eqIfPresent(StoreInfoDO::getStoreEnvImg, reqVO.getStoreEnvImg())
                .eqIfPresent(StoreInfoDO::getNotice, reqVO.getNotice())
                .eqIfPresent(StoreInfoDO::getLat, reqVO.getLat())
                .eqIfPresent(StoreInfoDO::getLon, reqVO.getLon())
                .eqIfPresent(StoreInfoDO::getAddress, reqVO.getAddress())
                .eqIfPresent(StoreInfoDO::getStatus, reqVO.getStatus())
//                .betweenIfPresent(StoreInfoDO::getTotalMoney, reqVO.getTotalMoney())
//                .betweenIfPresent(StoreInfoDO::getTotalWithdrawal, reqVO.getTotalWithdrawal())
                .betweenIfPresent(StoreInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(StoreInfoDO::getId)
        );
    }

    default List<StoreInfoDO> selectList(StoreInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StoreInfoDO>()
                .likeIfPresent(StoreInfoDO::getStoreName, reqVO.getStoreName())
                .likeIfPresent(StoreInfoDO::getCityName, reqVO.getCityName())
                .eqIfPresent(StoreInfoDO::getStoreEnvImg, reqVO.getStoreEnvImg())
                .eqIfPresent(StoreInfoDO::getNotice, reqVO.getNotice())
                .eqIfPresent(StoreInfoDO::getLat, reqVO.getLat())
                .eqIfPresent(StoreInfoDO::getLon, reqVO.getLon())
                .eqIfPresent(StoreInfoDO::getAddress, reqVO.getAddress())
                .eqIfPresent(StoreInfoDO::getStatus, reqVO.getStatus())
//                .betweenIfPresent(StoreInfoDO::getTotalMoney, reqVO.getTotalMoney())
//                .betweenIfPresent(StoreInfoDO::getTotalWithdrawal, reqVO.getTotalWithdrawal())
                .betweenIfPresent(StoreInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(StoreInfoDO::getId)
        );
    }

}
