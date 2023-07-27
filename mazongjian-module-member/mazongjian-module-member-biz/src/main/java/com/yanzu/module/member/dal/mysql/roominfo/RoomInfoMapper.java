package com.yanzu.module.member.dal.mysql.roominfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.roominfo.vo.*;

/**
 * 房间管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RoomInfoMapper extends BaseMapperX<RoomInfoDO> {

    default PageResult<RoomInfoDO> selectPage(RoomInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RoomInfoDO>()
                .likeIfPresent(RoomInfoDO::getRoomName, reqVO.getRoomName())
                .eqIfPresent(RoomInfoDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(RoomInfoDO::getType, reqVO.getType())
//                .betweenIfPresent(RoomInfoDO::getPrice, reqVO.getPrice())
                .eqIfPresent(RoomInfoDO::getSortId, reqVO.getSortId())
                .geIfPresent(RoomInfoDO::getBanTimeStart, reqVO.getBanTimeStart())
                .leIfPresent(RoomInfoDO::getBanTimeEnd, reqVO.getBanTimeEnd())
//                .betweenIfPresent(RoomInfoDO::getTotalOrderNum, reqVO.getTotalOrderNum())
//                .betweenIfPresent(RoomInfoDO::getTotalMoney, reqVO.getTotalMoney())
                .eqIfPresent(RoomInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(RoomInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(RoomInfoDO::getId)
        );
    }

    default List<RoomInfoDO> selectList(RoomInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RoomInfoDO>()
                .likeIfPresent(RoomInfoDO::getRoomName, reqVO.getRoomName())
                .eqIfPresent(RoomInfoDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(RoomInfoDO::getType, reqVO.getType())
//                .betweenIfPresent(RoomInfoDO::getPrice, reqVO.getPrice())
                .eqIfPresent(RoomInfoDO::getSortId, reqVO.getSortId())
                .geIfPresent(RoomInfoDO::getBanTimeStart, reqVO.getBanTimeStart())
                .leIfPresent(RoomInfoDO::getBanTimeEnd, reqVO.getBanTimeEnd())
//                .betweenIfPresent(RoomInfoDO::getTotalOrderNum, reqVO.getTotalOrderNum())
//                .betweenIfPresent(RoomInfoDO::getTotalMoney, reqVO.getTotalMoney())
                .eqIfPresent(RoomInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(RoomInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(RoomInfoDO::getId)
        );
    }

}
