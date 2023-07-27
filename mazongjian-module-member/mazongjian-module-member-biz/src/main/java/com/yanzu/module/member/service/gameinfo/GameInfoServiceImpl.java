package com.yanzu.module.member.service.gameinfo;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.gameinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.gameinfo.GameInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.gameinfo.GameInfoConvert;
import com.yanzu.module.member.dal.mysql.gameinfo.GameInfoMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 在线组局管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class GameInfoServiceImpl implements GameInfoService {

    @Resource
    private GameInfoMapper gameInfoMapper;


    @Override
    public void deleteGameInfo(Long id) {
        // 删除
        gameInfoMapper.deleteById(id);
    }



    @Override
    public GameInfoDO getGameInfo(Long id) {
        return gameInfoMapper.selectById(id);
    }



    @Override
    public PageResult<GameInfoDO> getGameInfoPage(GameInfoPageReqVO pageReqVO) {
        return gameInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<GameInfoDO> getGameInfoList(GameInfoExportReqVO exportReqVO) {
        return gameInfoMapper.selectList(exportReqVO);
    }

}
