package com.yanzu.module.member.service.game;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.game.vo.AppGameInfoReqVO;
import com.yanzu.module.member.controller.app.game.vo.AppGameInfoRespVO;
import com.yanzu.module.member.controller.app.game.vo.AppGamePageReqVO;

public interface AppGameService {
    void save(AppGameInfoReqVO reqVO);

    PageResult<AppGameInfoRespVO> getOrderPage(AppGamePageReqVO reqVO);

    void deleteUser(Long gameId, Long userId);

    void join(Long gameId);
}
