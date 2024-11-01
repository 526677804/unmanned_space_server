package com.yanzu.module.member.service.index;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.index.vo.*;

import java.util.List;

public interface IndexService {
    List<String> getCityList();

    List<AppBannerInfoRespVO> getBannerList();

    PageResult<AppStorePageRespVO> getStorePageList(AppStorePageReqVO reqVO);

    AppIndexStoreInfoRespVO getStoreInfo(Long storeId,String lat,String lon);

    List<KeyValue<String, Long>> getStoreList(String name,String cityName);

    List<KeyValue<String, Long>> getRoomList(Long storeId);

    List<KeyValue<String, Long>> getRoomListByAdmin(Long storeId);

    List<AppRoomInfoListRespVO> getRoomInfoList(AppRoomListReqVO reqVO);

    AppRoomInfoListRespVO getRoomInfo(Long roomId);

    AppSysInfoRespVO getSysInfo();


}
