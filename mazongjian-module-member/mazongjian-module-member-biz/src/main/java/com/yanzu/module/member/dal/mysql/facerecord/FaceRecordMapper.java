package com.yanzu.module.member.dal.mysql.facerecord;

import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.facerecord.FaceRecordDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.facerecord.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 人脸识别记录 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface FaceRecordMapper extends BaseMapperX<FaceRecordDO> {

    default PageResult<FaceRecordDO> selectPage(FaceRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FaceRecordDO>()
                .eqIfPresent(FaceRecordDO::getAdmitGuid, reqVO.getAdmitGuid())
                .betweenIfPresent(FaceRecordDO::getShowTime, reqVO.getShowTime())
                .eqIfPresent(FaceRecordDO::getType, reqVO.getType())
                .eqIfPresent(FaceRecordDO::getDeviceSn, reqVO.getDeviceSn())
                .orderByDesc(FaceRecordDO::getId));
    }

    default List<FaceRecordDO> selectList(FaceRecordExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<FaceRecordDO>()
                .eqIfPresent(FaceRecordDO::getAdmitGuid, reqVO.getAdmitGuid())
                .betweenIfPresent(FaceRecordDO::getShowTime, reqVO.getShowTime())
                .eqIfPresent(FaceRecordDO::getType, reqVO.getType())
                .betweenIfPresent(FaceRecordDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(FaceRecordDO::getDeviceSn, reqVO.getDeviceSn())
                .orderByDesc(FaceRecordDO::getId));
    }

    IPage<FaceRecordRespVO> getFaceRecordPage(@Param("page") IPage<FaceRecordRespVO> page, @Param("reqVO") FaceRecordPageReqVO reqVO
            , @Param("userId") Long userId, @Param("isAdmin") boolean isAdmin);

    FaceRecordRespVO getById(Long id);

    int updateBlacklist(@Param("id") Long id, @Param("type") Integer type, @Param("guid") String guid);

    int delGuid(@Param("admitGuid") String admitGuid);

}
