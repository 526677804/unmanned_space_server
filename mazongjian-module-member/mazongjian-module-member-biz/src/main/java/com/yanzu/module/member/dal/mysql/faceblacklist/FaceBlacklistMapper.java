package com.yanzu.module.member.dal.mysql.faceblacklist;

import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.faceblacklist.FaceBlacklistDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.faceblacklist.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 人脸黑名单 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface FaceBlacklistMapper extends BaseMapperX<FaceBlacklistDO> {

    default PageResult<FaceBlacklistDO> selectPage(FaceBlacklistPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FaceBlacklistDO>()
                .eqIfPresent(FaceBlacklistDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(FaceBlacklistDO::getAdmitGuid, reqVO.getAdmitGuid())
                .eqIfPresent(FaceBlacklistDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(FaceBlacklistDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FaceBlacklistDO::getBlacklistId));
    }


    IPage<FaceBlacklistRespVO> getFaceBlacklistPage(@Param("page") IPage<FaceBlacklistRespVO> page, @Param("reqVO") FaceBlacklistPageReqVO reqVO
            , @Param("userId") Long userId, @Param("isAdmin") boolean isAdmin);

    FaceBlacklistDO getByStoreAndGuid(@Param("storeId") Long storeId, @Param("admitGuid") String admitGuid);

}
