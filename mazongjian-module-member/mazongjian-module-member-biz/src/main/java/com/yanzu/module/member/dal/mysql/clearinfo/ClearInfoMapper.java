package com.yanzu.module.member.dal.mysql.clearinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 保洁信息管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ClearInfoMapper extends BaseMapperX<ClearInfoDO> {


    List<ClearInfoDO> getByUserIdAndStatusAndStoreIds(@Param("userId") Long userId, @Param("status") Integer status, @Param("storeIds") List<Long> storeIds);
    List<ClearInfoDO> getByUserIdAndStatusAndStoreId(@Param("userId") Long userId, @Param("status") Integer status, @Param("storeId") Long storeId);

}
