package com.yanzu.module.member.service.storeinfo;

import java.util.*;
import javax.validation.*;

import com.yanzu.module.member.controller.app.store.vo.*;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 门店管理 Service 接口
 *
 * @author 芋道源码
 */
public interface StoreInfoService {

    PageResult<AppStoreAdminRespVO> getPageList(AppStoreAdminReqVO reqVO);

    AppStoreInfoRespVO getDetail(Long storeId);

    void save(AppStoreInfoReqVO reqVO);

    List<AppRoomListRespVO> getRoomInfoList(Long storeId);

    AppRoomDetailRespVO getRoomDetail(Long roomId);

    void saveRoomDetail(AppRoomDetailReqVO reqVO);


    PageResult<AppDiscountRulesPageRespVO> getDiscountRulesPage(AppDiscountRulesPageReqVO reqVO);

    void changeDiscountRulesStatus(Long id);

    AppDiscountRulesDetailRespVO getDiscountRuleDetail(Long id);

    void saveDiscountRuleDetail(AppDiscountRulesDetailReqVO reqVO);



}
