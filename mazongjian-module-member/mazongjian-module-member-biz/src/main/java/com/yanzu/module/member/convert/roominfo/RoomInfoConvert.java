package com.yanzu.module.member.convert.roominfo;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.roominfo.vo.RoomInfoCreateReqVO;
import com.yanzu.module.member.controller.admin.roominfo.vo.RoomInfoExcelVO;
import com.yanzu.module.member.controller.admin.roominfo.vo.RoomInfoRespVO;
import com.yanzu.module.member.controller.admin.roominfo.vo.RoomInfoUpdateReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppRoomDetailReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppRoomDetailRespVO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 房间管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface RoomInfoConvert {

    RoomInfoConvert INSTANCE = Mappers.getMapper(RoomInfoConvert.class);

    RoomInfoDO convert(RoomInfoCreateReqVO bean);

    RoomInfoDO convert(RoomInfoUpdateReqVO bean);

    RoomInfoRespVO convert(RoomInfoDO bean);

    List<RoomInfoRespVO> convertList(List<RoomInfoDO> list);

    PageResult<RoomInfoRespVO> convertPage(PageResult<RoomInfoDO> page);

    List<RoomInfoExcelVO> convertList02(List<RoomInfoDO> list);

    AppRoomDetailRespVO convert2(RoomInfoDO roomInfoDO);

    RoomInfoDO convert3(AppRoomDetailReqVO reqVO);
}
