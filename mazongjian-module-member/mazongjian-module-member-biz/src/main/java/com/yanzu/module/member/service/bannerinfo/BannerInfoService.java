package com.yanzu.module.member.service.bannerinfo;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.bannerinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.bannerinfo.BannerInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 广告管理 Service 接口
 *
 * @author 芋道源码
 */
public interface BannerInfoService {

    /**
     * 创建广告管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBannerInfo(@Valid BannerInfoCreateReqVO createReqVO);

    /**
     * 更新广告管理
     *
     * @param updateReqVO 更新信息
     */
    void updateBannerInfo(@Valid BannerInfoUpdateReqVO updateReqVO);

    /**
     * 删除广告管理
     *
     * @param id 编号
     */
    void deleteBannerInfo(Long id);

    /**
     * 获得广告管理
     *
     * @param id 编号
     * @return 广告管理
     */
    BannerInfoDO getBannerInfo(Long id);

    /**
     * 获得广告管理列表
     *
     * @param ids 编号
     * @return 广告管理列表
     */
    List<BannerInfoDO> getBannerInfoList(Collection<Long> ids);

    /**
     * 获得广告管理分页
     *
     * @param pageReqVO 分页查询
     * @return 广告管理分页
     */
    PageResult<BannerInfoDO> getBannerInfoPage(BannerInfoPageReqVO pageReqVO);

    /**
     * 获得广告管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 广告管理列表
     */
    List<BannerInfoDO> getBannerInfoList(BannerInfoExportReqVO exportReqVO);

}
