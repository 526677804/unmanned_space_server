package com.yanzu.module.member.dal.mysql.roominfo;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.store.vo.AppRoomListRespVO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房间管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RoomInfoMapper extends BaseMapperX<RoomInfoDO> {


    List<KeyValue<String, Long>> getRoomList(@Param("storeId") Long storeId, @Param("userId") Long userId);

    List<AppRoomListRespVO> getRoomInfoList(Long storeId);

    int updateStatusById(@Param("status") Integer status, @Param("roomId") Long roomId);

    String getRoomImgs(Long roomId);

    int countByStoreIdAndUserId(@Param("storeId") Long storeId, @Param("userId") Long userId);
}
