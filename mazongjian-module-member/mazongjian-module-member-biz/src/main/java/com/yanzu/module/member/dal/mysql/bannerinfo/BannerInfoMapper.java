package com.yanzu.module.member.dal.mysql.bannerinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.index.vo.AppBannerInfoRespVO;
import com.yanzu.module.member.dal.dataobject.bannerinfo.BannerInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.bannerinfo.vo.*;

/**
 * 广告管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BannerInfoMapper extends BaseMapperX<BannerInfoDO> {

    default PageResult<BannerInfoDO> selectPage(BannerInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BannerInfoDO>()
                .eqIfPresent(BannerInfoDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BannerInfoDO::getType, reqVO.getType())
                .betweenIfPresent(BannerInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BannerInfoDO::getId));
    }

    default List<BannerInfoDO> selectList(BannerInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BannerInfoDO>()
                .eqIfPresent(BannerInfoDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BannerInfoDO::getType, reqVO.getType())
                .betweenIfPresent(BannerInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BannerInfoDO::getId));
    }

    List<AppBannerInfoRespVO> getBannerList(Integer type);
}
