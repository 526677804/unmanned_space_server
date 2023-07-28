package com.yanzu.module.member.service.index;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.bannerinfo.vo.BannerInfoBaseVO;
import com.yanzu.module.member.controller.app.index.vo.*;

import java.util.List;

public interface IndexService {
    List<String> getCityList();

    List<AppBannerInfoRespVO> getBannerList();

    PageResult<AppStorePageRespVO> getStorePageList(AppStorePageReqVO reqVO);

    AppIndexStoreInfoRespVO getStoreInfo(Long storeId);

    List<KeyValue<String, Long>> getStoreList(String name);

    List<KeyValue<String, Long>> getRoomList(Long storeId);

    List<AppRoomInfoListRespVO> getRoomInfoList(Long storeId);
}
