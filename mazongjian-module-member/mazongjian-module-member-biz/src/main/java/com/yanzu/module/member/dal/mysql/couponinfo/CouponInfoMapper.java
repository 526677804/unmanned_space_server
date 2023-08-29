package com.yanzu.module.member.dal.mysql.couponinfo;

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

    List<AppCouponPageRespVO> getCouponPage(AppCouponPageReqVO reqVO);

    CouponInfoDO getByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);

    List<AppCouponPageRespVO> getCouponPageByAdmin(@Param("reqVO") AppManagerCouponPageReqVO reqVO, @Param("storeIds") String storeIds);

    AppCouponDetailRespVO getCouponDetail(Long couponId);

    CouponInfoDO getByIdAndAdmin(Long couponId);

}
