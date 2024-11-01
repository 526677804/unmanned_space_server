package com.yanzu.module.member.dal.mysql.storeinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoExportReqVO;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoPageReqVO;
import com.yanzu.module.member.controller.app.index.vo.*;
import com.yanzu.module.member.controller.app.store.vo.AppStoreAdminReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreAdminRespVO;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


/**
 * 门店管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface StoreInfoMapper extends BaseMapperX<StoreInfoDO> {
    default PageResult<StoreInfoDO> selectPage(StoreInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StoreInfoDO>()
                .likeIfPresent(StoreInfoDO::getStoreName, reqVO.getStoreName())
                .likeIfPresent(StoreInfoDO::getCityName, reqVO.getCityName())
                .likeIfPresent(StoreInfoDO::getAddress, reqVO.getAddress())
                .eqIfPresent(StoreInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(StoreInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(StoreInfoDO::getStoreId));
    }

    default List<StoreInfoDO> selectList(StoreInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StoreInfoDO>()
                .likeIfPresent(StoreInfoDO::getStoreName, reqVO.getStoreName())
                .likeIfPresent(StoreInfoDO::getCityName, reqVO.getCityName())
                .likeIfPresent(StoreInfoDO::getAddress, reqVO.getAddress())
                .eqIfPresent(StoreInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(StoreInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(StoreInfoDO::getStoreId));
    }
    List<String> getCityList();

    IPage<AppStorePageRespVO> getStorePageList(@Param("page") IPage<AppStorePageRespVO> page, @Param("reqVO") AppStorePageReqVO reqVO, @Param("userId") Long userId);

    AppIndexStoreInfoRespVO getStoreInfo(@Param("storeId") Long storeId, @Param("lat") String lat, @Param("lon") String lon);

    List<KeyValue<String, Long>> getStoreList(@Param("name") String name,@Param("cityName") String cityName, @Param("userId") Long userId);

    List<AppRoomInfoListRespVO> getRoomInfoList(AppRoomListReqVO reqVO);

    AppRoomInfoListRespVO getRoomInfo(Long roomId);

    IPage<AppStoreAdminRespVO> getPageList(@Param("page") IPage<AppStoreAdminRespVO> page, @Param("reqVO") AppStoreAdminReqVO reqVO);

    List<KeyValue<String, Long>> getStoreListByMember(@Param("name") String name, @Param("cityName") String cityName);

    List<KeyValue<Long, String>> getNameMapByIds(Set<String> storeIdSet);

    List<StoreInfoDO> getListByIds(Set<String> storeIds);

    String getNameById(Long storeId);

    String getDouyinPoiId(Long storeId);

    boolean getOrderDoorOpen(Long storeId);

    int executeExpire();


    int renew(@Param("storeId") Long storeId, @Param("status") Integer status, @Param("newDate") LocalDateTime newDate);

}
