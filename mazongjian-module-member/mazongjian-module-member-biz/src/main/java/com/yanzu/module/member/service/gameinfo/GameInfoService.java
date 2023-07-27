package com.yanzu.module.member.service.gameinfo;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.gameinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.gameinfo.GameInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 在线组局管理 Service 接口
 *
 * @author 芋道源码
 */
public interface GameInfoService {


    /**
     * 删除在线组局管理
     *
     * @param id 编号
     */
    void deleteGameInfo(Long id);

    /**
     * 获得在线组局管理
     *
     * @param id 编号
     * @return 在线组局管理
     */
    GameInfoDO getGameInfo(Long id);


    /**
     * 获得在线组局管理分页
     *
     * @param pageReqVO 分页查询
     * @return 在线组局管理分页
     */
    PageResult<GameInfoDO> getGameInfoPage(GameInfoPageReqVO pageReqVO);

    /**
     * 获得在线组局管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 在线组局管理列表
     */
    List<GameInfoDO> getGameInfoList(GameInfoExportReqVO exportReqVO);

}
