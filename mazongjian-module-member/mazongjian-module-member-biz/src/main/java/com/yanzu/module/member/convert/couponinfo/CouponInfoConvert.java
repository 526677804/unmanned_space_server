package com.yanzu.module.member.convert.couponinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.couponinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;

/**
 * 优惠券管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface CouponInfoConvert {

    CouponInfoConvert INSTANCE = Mappers.getMapper(CouponInfoConvert.class);

    CouponInfoDO convert(CouponInfoCreateReqVO bean);

    CouponInfoDO convert(CouponInfoUpdateReqVO bean);

    CouponInfoRespVO convert(CouponInfoDO bean);

    List<CouponInfoRespVO> convertList(List<CouponInfoDO> list);

    PageResult<CouponInfoRespVO> convertPage(PageResult<CouponInfoDO> page);

    List<CouponInfoExcelVO> convertList02(List<CouponInfoDO> list);

}
