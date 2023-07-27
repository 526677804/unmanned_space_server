package com.yanzu.module.member.convert.gameinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.gameinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.gameinfo.GameInfoDO;

/**
 * 在线组局管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface GameInfoConvert {

    GameInfoConvert INSTANCE = Mappers.getMapper(GameInfoConvert.class);

    GameInfoDO convert(GameInfoCreateReqVO bean);

    GameInfoDO convert(GameInfoUpdateReqVO bean);

    GameInfoRespVO convert(GameInfoDO bean);

    List<GameInfoRespVO> convertList(List<GameInfoDO> list);

    PageResult<GameInfoRespVO> convertPage(PageResult<GameInfoDO> page);

    List<GameInfoExcelVO> convertList02(List<GameInfoDO> list);

}
