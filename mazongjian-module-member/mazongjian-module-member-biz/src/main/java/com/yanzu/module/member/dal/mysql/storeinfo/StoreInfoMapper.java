package com.yanzu.module.member.dal.mysql.storeinfo;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoExportReqVO;
import com.yanzu.module.member.controller.admin.storeinfo.vo.StoreInfoPageReqVO;
import com.yanzu.module.member.controller.app.index.vo.AppIndexStoreInfoRespVO;
import com.yanzu.module.member.controller.app.index.vo.AppRoomInfoListRespVO;
import com.yanzu.module.member.controller.app.index.vo.AppStorePageReqVO;
import com.yanzu.module.member.controller.app.index.vo.AppStorePageRespVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreAdminReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreAdminRespVO;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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


    List<AppStorePageRespVO> getStorePageList(AppStorePageReqVO reqVO);

    AppIndexStoreInfoRespVO getStoreInfo(Long storeId);

    List<KeyValue<String, Long>> getStoreList(@Param("name") String name,@Param("cityName") String cityName, @Param("userId") Long userId);

    List<AppRoomInfoListRespVO> getRoomInfoList(Long storeId);

    AppRoomInfoListRespVO getRoomInfo(Long roomId);

    List<AppStoreAdminRespVO> getPageList(AppStoreAdminReqVO reqVO);

    List<KeyValue<String, Long>> getStoreListByMember(@Param("name") String name, @Param("cityName") String cityName);

    List<KeyValue<Long, String>> getNameMapByIds(Set<String> storeIdSet);

    List<StoreInfoDO> getListByIds(Set<String> storeIds);

}
