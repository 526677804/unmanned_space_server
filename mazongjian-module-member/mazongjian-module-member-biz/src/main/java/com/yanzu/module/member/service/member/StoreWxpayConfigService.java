package com.yanzu.module.member.service.member;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.wxpay.vo.*;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 门店微信支付配置 Service 接口
 *
 * @author MrGuan
 */
public interface StoreWxpayConfigService {

    /**
     * 创建门店微信支付配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStoreWxpayConfig(@Valid StoreWxpayConfigCreateReqVO createReqVO);

    /**
     * 更新门店微信支付配置
     *
     * @param updateReqVO 更新信息
     */
    void updateStoreWxpayConfig(@Valid StoreWxpayConfigUpdateReqVO updateReqVO);

    /**
     * 删除门店微信支付配置
     *
     * @param id 编号
     */
    void deleteStoreWxpayConfig(Long id);

    /**
     * 获得门店微信支付配置
     *
     * @param id 编号
     * @return 门店微信支付配置
     */
    StoreWxpayConfigDO getStoreWxpayConfig(Long id);

    /**
     * 获得门店微信支付配置列表
     *
     * @param ids 编号
     * @return 门店微信支付配置列表
     */
    List<StoreWxpayConfigDO> getStoreWxpayConfigList(Collection<Long> ids);

    /**
     * 获得门店微信支付配置分页
     *
     * @param pageReqVO 分页查询
     * @return 门店微信支付配置分页
     */
    PageResult<StoreWxpayConfigPageRespVO> getStoreWxpayConfigPage(StoreWxpayConfigPageReqVO pageReqVO);

    /**
     * 获得门店微信支付配置列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 门店微信支付配置列表
     */
    List<StoreWxpayConfigDO> getStoreWxpayConfigList(StoreWxpayConfigExportReqVO exportReqVO);

    void profitsharing(Long id);
}
