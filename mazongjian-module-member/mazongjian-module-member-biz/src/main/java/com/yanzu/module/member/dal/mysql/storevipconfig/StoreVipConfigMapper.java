package com.yanzu.module.member.dal.mysql.storevipconfig;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.store.vo.AppStoreVipConfigListRespVO;
import com.yanzu.module.member.dal.dataobject.storevipconfig.StoreVipConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 门店会员配置 Mapper
 *
 * @author 超级管理员
 */
@Mapper
public interface StoreVipConfigMapper extends BaseMapperX<StoreVipConfigDO> {


    List<AppStoreVipConfigListRespVO> getVipConfig(Long storeId);

    int deleteByStoreId(Long storeId);

}
