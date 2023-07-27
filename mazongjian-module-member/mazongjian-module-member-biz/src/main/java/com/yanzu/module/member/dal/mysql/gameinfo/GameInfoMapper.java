package com.yanzu.module.member.dal.mysql.gameinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.gameinfo.GameInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.gameinfo.vo.*;

/**
 * 在线组局管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface GameInfoMapper extends BaseMapperX<GameInfoDO> {

    default PageResult<GameInfoDO> selectPage(GameInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<GameInfoDO>()
                .eqIfPresent(GameInfoDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(GameInfoDO::getRuleDesc, reqVO.getRuleDesc())
                .betweenIfPresent(GameInfoDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(GameInfoDO::getUserId, reqVO.getUserId())
                .eqIfPresent(GameInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(GameInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(GameInfoDO::getId)
        );
    }

    default List<GameInfoDO> selectList(GameInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<GameInfoDO>()
                .eqIfPresent(GameInfoDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(GameInfoDO::getRuleDesc, reqVO.getRuleDesc())
                .betweenIfPresent(GameInfoDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(GameInfoDO::getUserId, reqVO.getUserId())
                .eqIfPresent(GameInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(GameInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(GameInfoDO::getId)
        );
    }

}
