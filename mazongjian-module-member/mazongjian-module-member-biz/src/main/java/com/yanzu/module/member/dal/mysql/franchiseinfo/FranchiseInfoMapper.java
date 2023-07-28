package com.yanzu.module.member.dal.mysql.franchiseinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 加盟信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FranchiseInfoMapper extends BaseMapperX<FranchiseInfoDO> {


    FranchiseInfoDO getByUserId(Long userId);
}
