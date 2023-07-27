package com.yanzu.module.member.dal.mysql.storeuser;

import java.math.BigDecimal;
import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.storeuser.vo.*;

/**
 * 门店用户管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface StoreUserMapper extends BaseMapperX<StoreUserDO> {

    default PageResult<StoreUserDO> selectPage(StoreUserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StoreUserDO>()
                .betweenIfPresent(StoreUserDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(StoreUserDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(StoreUserDO::getUserId, reqVO.getUserId())
                .eqIfPresent(StoreUserDO::getType, reqVO.getType())
                .eqIfPresent(StoreUserDO::getStatus, reqVO.getStatus())
                .orderByDesc(StoreUserDO::getId));
    }

    default List<StoreUserDO> selectList(StoreUserExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StoreUserDO>()
                .betweenIfPresent(StoreUserDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(StoreUserDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(StoreUserDO::getUserId, reqVO.getUserId())
                .eqIfPresent(StoreUserDO::getType, reqVO.getType())
                .eqIfPresent(StoreUserDO::getStatus, reqVO.getStatus())
                .orderByDesc(StoreUserDO::getId));
    }

    BigDecimal getGiftBalanceByUserId(Long userId);

}
