package com.yanzu.module.member.service.clear;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.clear.vo.*;

public interface AppClearService {
    PageResult<AppClearPageRespVO> getClearPage(AppClearPageReqVO reqVO);

    void changeStatus(Long id, Integer status);

    void openStoreDoor(Long id);

    void openRoomDoor(Long id);

    AppClearInfoRespVO getDetail(Long clearId);

    AppClearChartRespVO getChartData();

    PageResult<AppClearBillRespVO> getClearBillPage(AppClearBillReqVO reqVO);

    void finish(AppStartClearReqVO reqVO);


}
