package com.yanzu.module.member.dal.mysql.couponinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.manager.vo.AppCouponDetailRespVO;
import com.yanzu.module.member.controller.app.manager.vo.AppManagerCouponPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageRespVO;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CouponInfoMapper extends BaseMapperX<CouponInfoDO> {


    Integer countByUserId(Long userId);

    IPage<AppCouponPageRespVO> getCouponPage(@Param("page") IPage<AppCouponPageRespVO> page, @Param("reqVO") AppCouponPageReqVO reqVO);

    CouponInfoDO getByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);

    IPage<AppCouponPageRespVO> getCouponPageByAdmin(@Param("page") IPage<AppCouponPageRespVO> page, @Param("reqVO") AppManagerCouponPageReqVO reqVO, @Param("storeIds") String storeIds);

    AppCouponDetailRespVO getCouponDetail(Long couponId);

    CouponInfoDO getByIdAndAdmin(Long couponId);

    int executeCouponExpire();

    int countNewUserByStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);
}
