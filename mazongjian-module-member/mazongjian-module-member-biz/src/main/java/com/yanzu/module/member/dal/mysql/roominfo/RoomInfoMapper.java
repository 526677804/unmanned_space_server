package com.yanzu.module.member.dal.mysql.roominfo;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.member.controller.admin.roominfo.vo.RoomInfoExportReqVO;
import com.yanzu.module.member.controller.admin.roominfo.vo.RoomInfoPageReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppRoomInfoListRespVO;
import com.yanzu.module.member.controller.app.store.vo.AppRoomListRespVO;
import com.yanzu.module.member.controller.app.store.vo.AppRoomListVO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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
                .eqIfPresent(RoomInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(RoomInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RoomInfoDO::getRoomId));
    }

    default List<RoomInfoDO> selectList(RoomInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RoomInfoDO>()
                .likeIfPresent(RoomInfoDO::getRoomName, reqVO.getRoomName())
                .eqIfPresent(RoomInfoDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(RoomInfoDO::getType, reqVO.getType())
                .eqIfPresent(RoomInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(RoomInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RoomInfoDO::getRoomId));
    }

    List<KeyValue<String, Long>> getRoomList(@Param("storeId") Long storeId, @Param("userId") Long userId);

    List<AppRoomListRespVO> getRoomInfoList(@Param("storeId") Long storeId, @Param("userId") Long userId);

    int updateStatusById(@Param("status") Integer status, @Param("roomId") Long roomId);

    int updateStatusByIds(@Param("status") Integer status, @Param("roomIds") String roomIds);

    String getRoomImgs(Long roomId);

    Integer countByStoreIdAndUserId(@Param("storeId") Long storeId, @Param("userId") Long userId);

    List<KeyValue<String, Long>> getRoomListByAdmin(Long storeId);

    List<KeyValue<String, Long>> getRoomListByStoreId(Long storeId);

    String getNameById(Long roomId);

    List<AppRoomListVO> getListByIds(@Param("roomIds") Set<Long> roomIds);
   AppRoomListVO getInfoById(@Param("roomId") Long roomId);

    List<AppRoomInfoListRespVO> getRoomInfoList2(@Param("storeIds") List<String> storeIds, @Param("storeId") Long storeId);

    List<Integer> getClassList(Long storeId);

}
