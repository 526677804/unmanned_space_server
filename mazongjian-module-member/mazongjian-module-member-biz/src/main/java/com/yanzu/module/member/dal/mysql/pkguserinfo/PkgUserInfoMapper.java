package com.yanzu.module.member.dal.mysql.pkguserinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.pkguserinfo.PkgUserInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户套餐信息 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface PkgUserInfoMapper extends BaseMapperX<PkgUserInfoDO> {

    int countByUserId(@Param("pkgId") Long pkgId, @Param("userId") Long userId);

    PkgUserInfoDO getByPkgId(@Param("userId") Long userId, @Param("pkgId") Long pkgId);

    PkgUserInfoDO getByOrderId(Long orderId);


}
