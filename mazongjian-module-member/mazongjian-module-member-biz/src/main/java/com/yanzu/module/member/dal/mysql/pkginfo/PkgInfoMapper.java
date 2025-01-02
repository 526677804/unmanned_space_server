package com.yanzu.module.member.dal.mysql.pkginfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.QueryWrapperX;
import com.yanzu.module.member.controller.app.pkg.vo.*;
import com.yanzu.module.member.dal.dataobject.pkginfo.PkgInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 套餐信息 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface PkgInfoMapper extends BaseMapperX<PkgInfoDO> {


    IPage<AppAdminPkgPageRespVO> getAdminPkgPage(@Param("page") IPage<AppAdminPkgPageRespVO> page, @Param("reqVO") AppAdminPkgPageReqVO reqVO
            , @Param("storeIds") String storeIds);

    IPage<AppPkgPageRespVO> getPkgPage(@Param("page") IPage<AppPkgPageRespVO> page, @Param("reqVO") AppPkgPageReqVO reqVO);

    IPage<AppPkgMyPageRespVO> getMyPkgPage(@Param("page") IPage<AppPkgMyPageRespVO> page, @Param("reqVO") AppMyPkgPageReqVO reqVO, @Param("userId") Long userId);

    PkgInfoDO getFirstPkgByRoomTypeOrRoomId(@Param("storeId") Long storeId, @Param("roomType") Integer roomType, @Param("roomId")Long roomId);

    default PkgInfoDO getPkgByShopId(String shopId){
        return selectOne(new QueryWrapperX<PkgInfoDO>()
                .eq("deleted", 0)
                .eq("enable", 1)
                .and(wrapper -> wrapper.eq("mt_id", shopId)
                        .or().eq("dy_id", shopId)
                        .or().eq("ks_id", shopId))
        );
    }

    int updateEnable(@Param("pkgId") Long pkgId, @Param("enable") boolean enable);
}
