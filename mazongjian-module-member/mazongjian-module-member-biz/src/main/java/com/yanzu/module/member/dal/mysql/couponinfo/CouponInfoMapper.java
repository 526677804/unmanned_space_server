package com.yanzu.module.member.dal.mysql.couponinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageRespVO;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.couponinfo.vo.*;

/**
 * 优惠券管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CouponInfoMapper extends BaseMapperX<CouponInfoDO> {

    default PageResult<CouponInfoDO> selectPage(CouponInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CouponInfoDO>()
                .eqIfPresent(CouponInfoDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CouponInfoDO::getCreateUserId, reqVO.getCreateUserId())
                .betweenIfPresent(CouponInfoDO::getExpriceTime, reqVO.getExpriceTime())
                .likeIfPresent(CouponInfoDO::getCouponName, reqVO.getCouponName())
//                .betweenIfPresent(CouponInfoDO::getMinUsePrice, reqVO.getMinUsePrice())
//                .betweenIfPresent(CouponInfoDO::getPrice, reqVO.getPrice())
//                .eqIfPresent(CouponInfoDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(CouponInfoDO::getType, reqVO.getType())
                .eqIfPresent(CouponInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CouponInfoDO::getCreateTime, reqVO.getCreateTime()));
//                .orderByDesc(CouponInfoDO::getId));
    }

    default List<CouponInfoDO> selectList(CouponInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CouponInfoDO>()
                .eqIfPresent(CouponInfoDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CouponInfoDO::getCreateUserId, reqVO.getCreateUserId())
                .betweenIfPresent(CouponInfoDO::getExpriceTime, reqVO.getExpriceTime())
                .likeIfPresent(CouponInfoDO::getCouponName, reqVO.getCouponName())
//                .betweenIfPresent(CouponInfoDO::getMinUsePrice, reqVO.getMinUsePrice())
//                .betweenIfPresent(CouponInfoDO::getPrice, reqVO.getPrice())
//                .eqIfPresent(CouponInfoDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(CouponInfoDO::getType, reqVO.getType())
                .eqIfPresent(CouponInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CouponInfoDO::getCreateTime, reqVO.getCreateTime()));
//                .orderByDesc(CouponInfoDO::getId))
    }

    Integer countByUserId(Long userId);

    List<AppCouponPageRespVO> getCouponPage(AppCouponPageReqVO reqVO);

}
