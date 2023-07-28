package com.yanzu.module.member.convert.roominfo;

import com.yanzu.module.member.controller.app.store.vo.AppRoomDetailReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppRoomDetailRespVO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 房间管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface RoomInfoConvert {

    RoomInfoConvert INSTANCE = Mappers.getMapper(RoomInfoConvert.class);


    RoomInfoDO convert(AppRoomDetailReqVO bean);


    AppRoomDetailRespVO convert2(RoomInfoDO bean);



}
