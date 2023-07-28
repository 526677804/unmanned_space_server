package com.yanzu.module.member.dal.mysql.couponinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageRespVO;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CouponInfoMapper extends BaseMapperX<CouponInfoDO> {


    Integer countByUserId(Long userId);

    List<AppCouponPageRespVO> getCouponPage(AppCouponPageReqVO reqVO);

}
