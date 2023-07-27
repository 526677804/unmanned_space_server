package com.yanzu.module.member.convert.roominfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.roominfo.vo.*;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;

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

}
