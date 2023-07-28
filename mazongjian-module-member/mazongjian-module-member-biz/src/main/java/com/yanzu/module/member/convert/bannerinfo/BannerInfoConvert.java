package com.yanzu.module.member.convert.bannerinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.bannerinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.bannerinfo.BannerInfoDO;

/**
 * 广告管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BannerInfoConvert {

    BannerInfoConvert INSTANCE = Mappers.getMapper(BannerInfoConvert.class);

    BannerInfoDO convert(BannerInfoCreateReqVO bean);

    BannerInfoDO convert(BannerInfoUpdateReqVO bean);

    BannerInfoRespVO convert(BannerInfoDO bean);

    List<BannerInfoRespVO> convertList(List<BannerInfoDO> list);

    PageResult<BannerInfoRespVO> convertPage(PageResult<BannerInfoDO> page);

    List<BannerInfoExcelVO> convertList02(List<BannerInfoDO> list);

}
