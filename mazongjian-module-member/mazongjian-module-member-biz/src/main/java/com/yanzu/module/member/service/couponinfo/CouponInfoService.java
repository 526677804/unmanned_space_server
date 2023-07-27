package com.yanzu.module.member.service.couponinfo;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.couponinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 优惠券管理 Service 接口
 *
 * @author 芋道源码
 */
public interface CouponInfoService {

    /**
     * 创建优惠券管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCouponInfo(@Valid CouponInfoCreateReqVO createReqVO);

    /**
     * 更新优惠券管理
     *
     * @param updateReqVO 更新信息
     */
    void updateCouponInfo(@Valid CouponInfoUpdateReqVO updateReqVO);

    /**
     * 删除优惠券管理
     *
     * @param id 编号
     */
    void deleteCouponInfo(Long id);

    /**
     * 获得优惠券管理
     *
     * @param id 编号
     * @return 优惠券管理
     */
    CouponInfoDO getCouponInfo(Long id);

    /**
     * 获得优惠券管理列表
     *
     * @param ids 编号
     * @return 优惠券管理列表
     */
    List<CouponInfoDO> getCouponInfoList(Collection<Long> ids);

    /**
     * 获得优惠券管理分页
     *
     * @param pageReqVO 分页查询
     * @return 优惠券管理分页
     */
    PageResult<CouponInfoDO> getCouponInfoPage(CouponInfoPageReqVO pageReqVO);

    /**
     * 获得优惠券管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 优惠券管理列表
     */
    List<CouponInfoDO> getCouponInfoList(CouponInfoExportReqVO exportReqVO);

}
