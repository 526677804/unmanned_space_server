package com.yanzu.module.member.service.storeinfo;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.storeinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.storeinfo.StoreInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 门店管理 Service 接口
 *
 * @author 芋道源码
 */
public interface StoreInfoService {

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
    StoreInfoDO getStoreInfo(Long id);

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

}
