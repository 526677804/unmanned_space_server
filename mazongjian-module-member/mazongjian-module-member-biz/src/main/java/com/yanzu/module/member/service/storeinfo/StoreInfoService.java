package com.yanzu.module.member.service.storeinfo;

import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.storeinfo.vo.*;
import com.yanzu.module.member.controller.app.store.vo.*;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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


    String uploadImg(InputStream inputStream);

    /**
     * 创建门店管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStoreInfo(@Valid StoreInfoCreateReqVO createReqVO);

    /**
     * 更新门店管理
     *
     * @param updateReqVO 更新信息
     */
    void updateStoreInfo(@Valid StoreInfoUpdateReqVO updateReqVO);

    /**
     * 删除门店管理
     *
     * @param id 编号
     */
    void deleteStoreInfo(Long id);

    /**
     * 获得门店管理
     *
     * @param id 编号
     * @return 门店管理
     */
    StoreInfoRespVO getStoreInfo(Long id);

    /**
     * 获得门店管理列表
     *
     * @param ids 编号
     * @return 门店管理列表
     */
    List<StoreInfoDO> getStoreInfoList(Collection<Long> ids);

    /**
     * 获得门店管理分页
     *
     * @param pageReqVO 分页查询
     * @return 门店管理分页
     */
    PageResult<StoreInfoDO> getStoreInfoPage(StoreInfoPageReqVO pageReqVO);

    /**
     * 获得门店管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 门店管理列表
     */
    List<StoreInfoDO> getStoreInfoList(StoreInfoExportReqVO exportReqVO);

    List<KeyValue<String, Long>> getStoreList(String name, String cityName);

    List<KeyValue<String, Long>> getRoomList(Long storeId);

    void checkPermisson(Long storeId, Long userId, Integer userType, Integer checkType);

    List<KeyValue<Long, String>> getNameMapByIds(Set<String> storeIdSet);

    void clearAndFinish(Long roomId);

    void finishRoomOrder(Long roomId);

    String meituanScope(Long storeId);

    void disableRoom(Long roomId);

    void syncPrice(Long storeId);

    void deleteRoomInfo(Long roomId);

    List<AppRoomInfoListRespVO> getRoomInfoList2(Long storeId);

    String getGroupPayAuthUrl(GroupPayAuthUrlReqVO reqVO);

    AppStoreSoundInfoRespVO getStoreSoundInfo(Long storeId);

    void saveStoreSoundInfo(AppStoreSoundInfoReqVO reqVO);

    void addDevice(AppAddDeviceReqVO reqVO);

    void delDevice(Long deviceId);

    void controlKT(AppStoreControlKTReqVO reqVO);

    void renew(StoreRenewReqVO reqVO);

    void addLock(AppAddLockReqVO reqVO);
}
